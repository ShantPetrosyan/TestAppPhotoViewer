package com.photogallery.app.data

interface Mapper<I, O> {
    fun map(input: I): O
}