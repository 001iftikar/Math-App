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
public final class BookRepositoryImpl_Factory implements Factory<BookRepositoryImpl> {
  private final Provider<FirebaseDatabase> firebaseDatabaseProvider;

  public BookRepositoryImpl_Factory(Provider<FirebaseDatabase> firebaseDatabaseProvider) {
    this.firebaseDatabaseProvider = firebaseDatabaseProvider;
  }

  @Override
  public BookRepositoryImpl get() {
    return newInstance(firebaseDatabaseProvider.get());
  }

  public static BookRepositoryImpl_Factory create(
      Provider<FirebaseDatabase> firebaseDatabaseProvider) {
    return new BookRepositoryImpl_Factory(firebaseDatabaseProvider);
  }

  public static BookRepositoryImpl newInstance(FirebaseDatabase firebaseDatabase) {
    return new BookRepositoryImpl(firebaseDatabase);
  }
}
