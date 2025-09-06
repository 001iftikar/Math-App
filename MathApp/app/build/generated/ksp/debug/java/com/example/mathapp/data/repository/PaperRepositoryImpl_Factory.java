package com.example.mathapp.data.repository;

import com.google.firebase.database.FirebaseDatabase;
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
public final class PaperRepositoryImpl_Factory implements Factory<PaperRepositoryImpl> {
  private final Provider<FirebaseDatabase> firebaseDatabaseProvider;

  public PaperRepositoryImpl_Factory(Provider<FirebaseDatabase> firebaseDatabaseProvider) {
    this.firebaseDatabaseProvider = firebaseDatabaseProvider;
  }

  @Override
  public PaperRepositoryImpl get() {
    return newInstance(firebaseDatabaseProvider.get());
  }

  public static PaperRepositoryImpl_Factory create(
      Provider<FirebaseDatabase> firebaseDatabaseProvider) {
    return new PaperRepositoryImpl_Factory(firebaseDatabaseProvider);
  }

  public static PaperRepositoryImpl newInstance(FirebaseDatabase firebaseDatabase) {
    return new PaperRepositoryImpl(firebaseDatabase);
  }
}
