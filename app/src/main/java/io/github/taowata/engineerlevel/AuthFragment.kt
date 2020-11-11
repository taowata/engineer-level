package io.github.taowata.engineerlevel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class AuthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val document = Jsoup.connect("https://github.com/taowata").get()
            Log.i("parse", document.select("h2.f4.text-normal.mb-2").last().text().split(" ").map { it.toString() }.first())
        }


//        val text = document.select(".f4 text-normal mb-2").first().text()
//        GitHubHomeHtmlParser.getContributionNumber("https://github.com/taowata")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }


}