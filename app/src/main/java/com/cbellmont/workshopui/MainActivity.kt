package com.cbellmont.workshopui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbellmont.datamodel.EventType
import com.cbellmont.datamodel.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_details.*
import kotlinx.android.synthetic.main.layout_welcome.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface UserSelectable {
    fun onUserSelected(user: User)
}

class MainActivity : AppCompatActivity(), UserSelectable {

    private lateinit var viewModel : MainActivityViewModel
    private var adapter = UserAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createLinearRecyclerView()

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            MainActivityViewModel::class.java
        )

        observeLoading()
        observeUsers()

        viewModel.downloadData()
    }

    private fun observeUsers() {
        viewModel.userList.observe(this, {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Main) {
                    viewModel.userList.value?.toList()
                        ?.let { listNotNull -> adapter.updateData(listNotNull) }
                }
            }
        })
    }

    private fun observeLoading(){
        viewModel.mainActivityStatus.observe(this, {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Main) {
                    viewModel.mainActivityStatus.value?.let {
                        when (it) {
                            MainActivityViewModel.MainActivityStatus.FINISHED -> {
                                hideLoading()
                            }
                            MainActivityViewModel.MainActivityStatus.WAITING -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Ha habido un error en la descarga",
                                    Toast.LENGTH_LONG
                                ).show()
                                hideLoading()
                            }
                            MainActivityViewModel.MainActivityStatus.LOADING -> {
                                showLoading()
                            }
                        }
                    }
                }
            }
        })
    }

    private fun showLoading(){
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        progressBar.visibility = View.INVISIBLE
    }

    private fun createLinearRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }

    override fun onUserSelected(user: User) {
        configureDetails(user)
    }

    private fun configureDetails(user: User) {
        clearDetails()
        layoutWelcome.visibility = View.GONE
        layoutDetails.visibility = View.VISIBLE

        Picasso.get().load(user.getLargePhoto()).into(ivPictureBig)
        user.events.forEach {
            when (it.name) {
                EventType.BIRTHDAY -> {
                    layoutBirthday.visibility = View.VISIBLE
                    tvBirthdayDetails.text = it.giftIdea.toString()
                }
                EventType.WEDDING -> {
                    layoutWedding.visibility = View.VISIBLE
                    tvWeddingDetails.text = it.giftIdea.toString()
                }
                EventType.PROMOTION -> {
                    layoutPromotion.visibility = View.VISIBLE
                    tvPromotionDetails.text = it.giftIdea.toString()
                }
                EventType.ANNIVERSARY -> {
                    layoutAnniversary.visibility = View.VISIBLE
                    tvAnniversaryDetails.text = it.giftIdea.toString()
                }
                EventType.OTHERS -> {
                    layoutOthers.visibility = View.VISIBLE
                    tvOthersDetails.text = it.giftIdea.toString()
                }
            }
        }

    }

    private fun clearDetails() {
        layoutBirthday.visibility = View.GONE
        layoutWedding.visibility = View.GONE
        layoutAnniversary.visibility = View.GONE
        layoutPromotion.visibility = View.GONE
        layoutOthers.visibility = View.GONE

        tvBirthdayDetails.text = ""
        tvAnniversaryDetails.text = ""
        tvWeddingDetails.text = ""
        tvPromotionDetails.text = ""
        tvOthersDetails.text = ""

    }
}