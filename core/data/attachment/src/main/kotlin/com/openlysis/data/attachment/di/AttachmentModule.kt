package com.openlysis.data.attachment.di

import android.content.Context
import com.openlysis.data.attachment.AttachmentFactory
import com.openlysis.data.attachment.DefaultAttachmentFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides dependencies for file attachment creation functionality.
 * This module is installed in the SingletonComponent, ensuring singleton scoped instances.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object AttachmentModule {
    @Singleton
    @Provides
    fun provideAttachmentFactory(
        @ApplicationContext context: Context
    ): AttachmentFactory = DefaultAttachmentFactory(context.contentResolver)
}