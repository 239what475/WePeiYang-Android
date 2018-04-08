package com.twt.service.settings

import agency.tango.materialintroscreen.SlideFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.twt.service.R
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator.REMOTE
import com.twtstudio.retrox.auth.api.authSelfLiveData
import com.twtstudio.retrox.tjulibrary.provider.TjuLibProvider

/**
 * Created by retrox on 01/03/2017.
 */

class LibBindFragment : SlideFragment() {

    @BindView(R.id.lib_password)
    private lateinit var libPasswordEdit: EditText
    @BindView(R.id.btn_lib_bind)
    private lateinit var button: Button
    private lateinit var unbinder: Unbinder

    @OnClick(R.id.btn_lib_bind)
    fun bind(view: View) {
        TjuLibProvider(context).bindLibrary({ integer ->
            when (integer) {
                -1 -> Toast.makeText(this.context, "图书馆绑定完成，点击底部右侧对勾开始新旅程", Toast.LENGTH_SHORT).show()
                50003 -> Toast.makeText(this.context, "图书馆已绑定，点击底部右侧对勾开始新旅程", Toast.LENGTH_SHORT).show()
                50002 -> Toast.makeText(this.context, "图书馆密码错误", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(this.context, "未知错误", Toast.LENGTH_SHORT).show()
            }
            authSelfLiveData.refresh(REMOTE)
        }, libPasswordEdit.text.toString().takeIf(String::isNotEmpty) ?: "000000")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_lib_bind_slide, container, false).also {
                unbinder = ButterKnife.bind(this, it)
            }

    override fun backgroundColor(): Int = R.color.white_color

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    override fun buttonsColor(): Int = R.color.intro_slide_buttons

    override fun canMoveFurther() = true

    override fun cantMoveFurtherErrorMessage() = "请绑定图书馆账号！"
}