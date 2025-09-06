package com.example.mathapp.ui.drawer;

import com.example.mathapp.domain.repository.AiRepository;
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
public final class AiViewModel_Factory implements Factory<AiViewModel> {
  private final Provider<AiRepository> aiRepositoryProvider;

  public AiViewModel_Factory(Provider<AiRepository> aiRepositoryProvider) {
    this.aiRepositoryProvider = aiRepositoryProvider;
  }

  @Override
  public AiViewModel get() {
    return newInstance(aiRepositoryProvider.get());
  }

  public static AiViewModel_Factory create(Provider<AiRepository> aiRepositoryProvider) {
    return new AiViewModel_Factory(aiRepositoryProvider);
  }

  public static AiViewModel newInstance(AiRepository aiRepository) {
    return new AiViewModel(aiRepository);
  }
}
