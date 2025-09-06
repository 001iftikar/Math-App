package com.example.mathapp.data.repository;

import com.google.firebase.ai.GenerativeModel;
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
public final class AiRepositoryImpl_Factory implements Factory<AiRepositoryImpl> {
  private final Provider<GenerativeModel> modelProvider;

  public AiRepositoryImpl_Factory(Provider<GenerativeModel> modelProvider) {
    this.modelProvider = modelProvider;
  }

  @Override
  public AiRepositoryImpl get() {
    return newInstance(modelProvider.get());
  }

  public static AiRepositoryImpl_Factory create(Provider<GenerativeModel> modelProvider) {
    return new AiRepositoryImpl_Factory(modelProvider);
  }

  public static AiRepositoryImpl newInstance(GenerativeModel model) {
    return new AiRepositoryImpl(model);
  }
}
