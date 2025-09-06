package com.example.mathapp.ui.study;

import com.example.mathapp.domain.repository.PaperRepository;
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
public final class PaperViewModel_Factory implements Factory<PaperViewModel> {
  private final Provider<PaperRepository> paperRepositoryProvider;

  public PaperViewModel_Factory(Provider<PaperRepository> paperRepositoryProvider) {
    this.paperRepositoryProvider = paperRepositoryProvider;
  }

  @Override
  public PaperViewModel get() {
    return newInstance(paperRepositoryProvider.get());
  }

  public static PaperViewModel_Factory create(Provider<PaperRepository> paperRepositoryProvider) {
    return new PaperViewModel_Factory(paperRepositoryProvider);
  }

  public static PaperViewModel newInstance(PaperRepository paperRepository) {
    return new PaperViewModel(paperRepository);
  }
}
