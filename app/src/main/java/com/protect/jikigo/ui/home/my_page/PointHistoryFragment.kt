package com.protect.jikigo.ui.home.my_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentPointHistoryBinding
import com.protect.jikigo.ui.adapter.PointHistoryAdapter
import com.protect.jikigo.ui.extensions.statusBarColor
import java.time.LocalDate
import java.util.Calendar
import kotlin.time.Duration.Companion.days


class PointHistoryFragment : Fragment() {
    private var _binding: FragmentPointHistoryBinding? = null
    private val binding get() = _binding!!
    private val adapter: PointHistoryAdapter by lazy { PointHistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPointHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBar()
        setLayout()
    }

    private fun setStatusBar() {
        requireActivity().statusBarColor(R.color.primary)
    }

    private fun setLayout() {
        onClickToolbar()
        recycler()
        calender()
    }

    private fun onClickToolbar() {
        binding.toolbarPointHistory.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun recycler() {
        binding.recyclerPointHistory.adapter = adapter
    }

    /*
    달력
     */
    private fun calender() {
        binding.apply {
            setTodayDate()

            calendarPointHistory.setOnDateChangeListener { view, year, month, dayOfMonth ->
                val dateText = getFormattedDate(year, month, dayOfMonth)
                tvPointHistoryDate.text = dateText
            }
        }
    }

    // 📌 날짜를 "20일 화요일" 형식으로 변환하는 함수
    private fun getFormattedDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val days = arrayOf("일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일")
        val dayOfWeekStr = days[dayOfWeek - 1] // 요일 변환

        return "${dayOfMonth}일 $dayOfWeekStr"
    }

    // 📌 오늘 날짜를 가져와서 텍스트뷰에 설정하는 함수
    private fun setTodayDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val todayDate = getFormattedDate(year, month, day)
        binding.tvPointHistoryDate.text = todayDate
    }

}