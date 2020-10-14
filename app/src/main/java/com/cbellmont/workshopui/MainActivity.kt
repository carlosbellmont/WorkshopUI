package com.cbellmont.workshopui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbellmont.datamodel.User
import kotlinx.android.synthetic.main.activity_main.*
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
        progressBar.visibility = View.GONE
    }

    private fun createLinearRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onUserSelected(user: User) {
        tvDetails.text = user.toString()
    }
}