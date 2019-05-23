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

@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@BindingAnnotation
annotation class AuthenticationStored

@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY,AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@BindingAnnotation
annotation class MemberDetailsStored

@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY,AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@BindingAnnotation
annotation class MemberIdStored

@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY,AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@BindingAnnotation
annotation class StatisticsStored