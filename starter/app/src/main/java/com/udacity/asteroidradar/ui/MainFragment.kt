package com.udacity.asteroidradar.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.ui.MainFragmentDirections
import com.udacity.asteroidradar.viewmodel.MainViewModel
import com.udacity.asteroidradar.viewmodel.MainViewModelFactory

class MainFragment : Fragment() {

    lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val dataSource = AsteroidDatabase.getInstance(application).asteroidDatabaseDao

        val viewModelFactory = MainViewModelFactory(dataSource, application)

        viewModel = ViewModelProvider(
            this, viewModelFactory).get(MainViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Assign the appropriate Adapter to this Fragment's RecyclerView
        val adapter = AsteroidAdapter(asteroidListener)
        binding.asteroidRecycler.adapter = adapter

        // Observe view model
        observeImage(binding)
        observeAsteroids(adapter)
        observeNavigation()
        observeToastMessage()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_show_week -> {
                showWeek()
                true
            }
            R.id.menu_item_show_today -> {
                showToday()
                return true
            }
            R.id.menu_item_show_saved -> {
                showSaved()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showWeek() {
        viewModel.showWeek()
    }

    private fun showToday() {
        viewModel.showToday()
    }

    private fun showSaved() {
        viewModel.showAllSaved()
    }

    // Event listeners

    private val asteroidListener = AsteroidListener { asteroid ->
        viewModel.onAsteroidTapped(asteroid)
    }

    // Observe data

    private fun observeImage(binding: FragmentMainBinding) {
        // Observe picture of day data on view model, update image
        viewModel.pictureOfDayUrl.observe(viewLifecycleOwner, Observer {
            it?.let {
                val imageView = binding.activityMainImageOfTheDay
                Picasso.with(context)
                        .load(it)
                        .centerCrop()
                        .fit()
                        .into(imageView)
            }
        })
    }

    private fun observeAsteroids(adapter: AsteroidAdapter) {
        // Observe asteroids data on view model, update adapter
        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })
    }

    private fun observeNavigation() {
        // Observe navigation flags on view model
        viewModel.navigateToAsteroid.observe(viewLifecycleOwner, Observer { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.onAsteroidNavigated()
            }
        })
    }

    private fun observeToastMessage() {
        // Observe messages to display in Toast
        viewModel.toastMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                viewModel.onToastDisplayed()
            }
        })
    }

}

