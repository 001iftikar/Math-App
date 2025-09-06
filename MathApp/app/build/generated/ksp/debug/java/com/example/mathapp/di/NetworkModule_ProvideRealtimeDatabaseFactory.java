package com.example.mathapp.di;

import com.google.firebase.database.FirebaseDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class NetworkModule_ProvideRealtimeDatabaseFactory implements Factory<FirebaseDatabase> {
  @Override
  public FirebaseDatabase get() {
    return provideRealtimeDatabase();
  }

  public static NetworkModule_ProvideRealtimeDatabaseFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FirebaseDatabase provideRealtimeDatabase() {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideRealtimeDatabase());
  }

  private static final class InstanceHolder {
    static final NetworkModule_ProvideRealtimeDatabaseFactory INSTANCE = new NetworkModule_ProvideRealtimeDatabaseFactory();
  }
}
