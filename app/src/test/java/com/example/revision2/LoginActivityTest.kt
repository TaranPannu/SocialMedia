package com.example.revision2

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class LoginActivityTest {
    @Test
    fun Empty_user(){
        val result=LoginActivity.Check_email_password("","123456")
        assertThat(result).isFalse()// import from truth library
    }
    @Test
    fun Empty_pass(){
        val result=LoginActivity.Check_email_password("Tom","")
        assertThat(result).isFalse()// import from truth library
    }
    @Test
    fun correct(){
        val result=LoginActivity.Check_email_password("Tom","123456")
        assertThat(result).isTrue()// import from truth library
    }
    @Test
    fun short_len_pass(){
        val result=LoginActivity.Check_email_password("Tom","12345")
        assertThat(result).isFalse()// import from truth library
    }
}