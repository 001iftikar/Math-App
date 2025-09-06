package com.example.mathapp.ui.teacher;

import com.example.mathapp.domain.repository.TeacherRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class TeacherViewModel_Factory implements Factory<TeacherViewModel> {
  private final Provider<TeacherRepository> teacherRepositoryProvider;

  public TeacherViewModel_Factory(Provider<TeacherRepository> teacherRepositoryProvider) {
    this.teacherRepositoryProvider = teacherRepositoryProvider;
  }

  @Override
  public TeacherViewModel get() {
    return newInstance(teacherRepositoryProvider.get());
  }

  public static TeacherViewModel_Factory create(
      Provider<TeacherRepository> teacherRepositoryProvider) {
    return new TeacherViewModel_Factory(teacherRepositoryProvider);
  }

  public static TeacherViewModel newInstance(TeacherRepository teacherRepository) {
    return new TeacherViewModel(teacherRepository);
  }
}
