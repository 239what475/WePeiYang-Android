package xyz.rickygao.gpa2.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import xyz.rickygao.gpa2.R
import xyz.rickygao.gpa2.api.Evaluate

/**
 * Created by rickygao on 2017/11/14.
 */
class CourseAdapter(val context: Context, val inflater: LayoutInflater) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    companion object {
        const val SORT_DEFAULT = 0
        const val SORT_BY_SCORE_DESC = 1
        const val SORT_BY_CREDIT_DESC = 2
    }

    var courses: MutableList<Course> = mutableListOf()
        set(value) {
            field = value
            ensureCourses()
        }

    var sortMode = SORT_DEFAULT
        set(value) {
            if (field == value) return
            field = value
            ensureCourses()
        }

    private fun ensureCourses() {
        when (sortMode) {
            SORT_BY_CREDIT_DESC ->
                courses.sortByDescending(Course::credit)
            SORT_BY_SCORE_DESC ->
                courses.sortByDescending(Course::score)
            else -> return
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CourseViewHolder {
        val itemView = inflater.inflate(R.layout.gpa2_item_course, parent, false)
        return CourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(courses.get(position))
    }

    override fun getItemCount(): Int = courses.size

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTv = itemView.findViewById<TextView>(R.id.tv_course_name)
        private val typeTv = itemView.findViewById<TextView>(R.id.tv_course_type)
        private val creditTv = itemView.findViewById<TextView>(R.id.tv_course_credit)
        private val scoreTv = itemView.findViewById<TextView>(R.id.tv_course_score)

        fun bind(course: Course) {
            nameTv.text = course.name

            // TODO: Now types are unavailable (or deprecated?), fix it at the back end or remove it.
            typeTv.text = when (course.type) {
                    0 -> "必修"
                    else -> "未知"
                }

            creditTv.text = "学分：${course.credit}"

            if (course.evaluate == null) {
                scoreTv.text = "成绩：${course.score}"
                itemView.setOnClickListener(null)

            } else {
                scoreTv.text = "点按来评价"
                itemView.setOnClickListener {
                    startEvaluateActivty(context, course.evaluate)
                }
            }
            }

    }

    data class Course(val name: String, val type: Int, val credit: Double, val score: Double, val evaluate: Evaluate?)
}