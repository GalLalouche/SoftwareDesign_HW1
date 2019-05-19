package il.ac.technion.cs.softwaredesign.managers

import com.google.inject.BindingAnnotation

@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@BindingAnnotation
annotation class UserIdSeqGenerator

@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@BindingAnnotation
annotation class ChannelIdSeqGenerator