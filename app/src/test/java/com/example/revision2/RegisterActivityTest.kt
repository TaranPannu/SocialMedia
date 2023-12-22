package com.example.revision2;

import com.google.common.truth.Truth
import org.junit.Test


class RegisterActivityTest {
     @Test
     fun Empty_user(){
         val result=RegisterActivity.RegisterActivity_Tests("","123456")
         Truth.assertThat(result).isFalse()// import from truth library
     }
     @Test
     fun Empty_pass(){
         val result=RegisterActivity.RegisterActivity_Tests("Tom","")
         Truth.assertThat(result).isFalse()// import from truth library
     }
     @Test
     fun correct(){
         val result=RegisterActivity.RegisterActivity_Tests("Tom","123456")
         Truth.assertThat(result).isTrue()// import from truth library
     }
     @Test
     fun short_len_pass(){
         val result=RegisterActivity.RegisterActivity_Tests("Tom","12345")
         Truth.assertThat(result).isFalse()// import from truth library
     }
}