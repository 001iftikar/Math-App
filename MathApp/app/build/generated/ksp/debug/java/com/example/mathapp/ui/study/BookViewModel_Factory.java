package com.example.mathapp.ui.study;

import com.example.mathapp.domain.repository.BookRepository;
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
public final class BookViewModel_Factory implements Factory<BookViewModel> {
  private final Provider<BookRepository> bookRepositoryProvider;

  public BookViewModel_Factory(Provider<BookRepository> bookRepositoryProvider) {
    this.bookRepositoryProvider = bookRepositoryProvider;
  }

  @Override
  public BookViewModel get() {
    return newInstance(bookRepositoryProvider.get());
  }

  public static BookViewModel_Factory create(Provider<BookRepository> bookRepositoryProvider) {
    return new BookViewModel_Factory(bookRepositoryProvider);
  }

  public static BookViewModel newInstance(BookRepository bookRepository) {
    return new BookViewModel(bookRepository);
  }
}
