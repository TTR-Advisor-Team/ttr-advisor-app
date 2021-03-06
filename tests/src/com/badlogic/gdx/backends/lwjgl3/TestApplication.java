//package com.badlogic.gdx.backends.lwjgl3;
//
//import com.badlogic.gdx.*;
//import com.badlogic.gdx.backends.headless.*;
//import com.badlogic.gdx.backends.headless.mock.audio.MockAudio;
//import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
//import com.badlogic.gdx.backends.lwjgl3.Lwjgl3NativesLoader;
//import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
//import com.badlogic.gdx.backends.lwjgl3.audio.Lwjgl3Audio;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.Clipboard;
//import com.badlogic.gdx.utils.ObjectMap;
//
//import static com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application.*;
//import static org.lwjgl.glfw.GLFW.glfwTerminate;
//
//import org.lwjgl.glfw.GLFW;
//import org.lwjgl.glfw.GLFWErrorCallback;
//
///**
// * Similar to {@link HeadlessApplication}, except that it provides a real OpenGL context using LWJGL3. This makes it
// * possible to test things that use features that depend on OpenGL, like a {@link SpriteBatch}.
// */
//public class TestApplication implements Application {
//  private final ObjectMap<String, Preferences> preferences = new ObjectMap<>();
//  private final Array<Runnable> runnables = new Array<>();
//  private final Array<Runnable> executedRunnables = new Array<>();
//  private final Array<LifecycleListener> lifecycleListeners = new Array<>();
//  private final ApplicationListener applicationListener;
//  private final Audio audio;
//  private final Files files;
//  private final Net net;
//  private final Lwjgl3Window window;
//  protected boolean running = true;
//  private ApplicationLogger applicationLogger;
//  private int logLevel = LOG_INFO;
//
//  public TestApplication(final ApplicationListener applicationListener) {
//    HeadlessNativesLoader.load();
//
//    Lwjgl3NativesLoader.load();
//
//    // GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
//
//    setApplicationLogger(new HeadlessApplicationLogger());
//
//    this.applicationListener = applicationListener;
//
//    Gdx.app = this;
//    Gdx.audio = this.audio = new MockAudio();
//    Gdx.files = this.files = new HeadlessFiles();
//    Gdx.net = this.net = new HeadlessNet(new HeadlessApplicationConfiguration());
//
//    final Lwjgl3ApplicationConfiguration windowConfig = new Lwjgl3ApplicationConfiguration();
//    windowConfig.setWindowedMode(854, 480);
//    windowConfig.setInitialVisible(false);
//    windowConfig.setTitle("Test");
//    
//    final Lwjgl3ApplicationBase application = new Lwjgl3ApplicationBase() {
//		
//		@Override
//		public void setLogLevel(int logLevel) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void setApplicationLogger(ApplicationLogger applicationLogger) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void removeLifecycleListener(LifecycleListener listener) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void postRunnable(Runnable runnable) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void log(String tag, String message, Throwable exception) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void log(String tag, String message) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public int getVersion() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//		
//		@Override
//		public ApplicationType getType() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//		@Override
//		public Preferences getPreferences(String name) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//		@Override
//		public Net getNet() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//		@Override
//		public long getNativeHeap() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//		
//		@Override
//		public int getLogLevel() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//		
//		@Override
//		public long getJavaHeap() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//		
//		@Override
//		public Input getInput() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//		@Override
//		public Graphics getGraphics() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//		@Override
//		public Files getFiles() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//		@Override
//		public Clipboard getClipboard() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//		@Override
//		public Audio getAudio() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//		@Override
//		public ApplicationLogger getApplicationLogger() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//		@Override
//		public ApplicationListener getApplicationListener() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//		@Override
//		public void exit() {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void error(String tag, String message, Throwable exception) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void error(String tag, String message) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void debug(String tag, String message, Throwable exception) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void debug(String tag, String message) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void addLifecycleListener(LifecycleListener listener) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public Lwjgl3Input createInput(Lwjgl3Window window) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//		@Override
//		public Lwjgl3Audio createAudio(Lwjgl3ApplicationConfiguration config) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//	};
//
//    this.window = new Lwjgl3Window(applicationListener, windowConfig, application);
//    final long windowHandle = createGlfwWindow(windowConfig, 0);
//    window.create(windowHandle);
//    window.makeCurrent();
//  }
//
//  public void render() {
//    boolean shouldRequestRendering;
//
//    synchronized (runnables) {
//      shouldRequestRendering = runnables.size > 0;
//      executedRunnables.clear();
//      executedRunnables.addAll(runnables);
//      runnables.clear();
//    }
//
//    for (final Runnable runnable : new Array.ArrayIterator<>(executedRunnables)) {
//      runnable.run();
//    }
//
//    if (shouldRequestRendering && !window.getGraphics().isContinuousRendering())
//      window.requestRendering();
//  }
//
//  @Override
//  public ApplicationListener getApplicationListener() {
//    return applicationListener;
//  }
//
//  @Override
//  public Graphics getGraphics() {
//    return window.getGraphics();
//  }
//
//  @Override
//  public Audio getAudio() {
//    return audio;
//  }
//
//  @Override
//  public Input getInput() {
//    return window.getInput();
//  }
//
//  @Override
//  public Files getFiles() {
//    return files;
//  }
//
//  @Override
//  public Net getNet() {
//    return net;
//  }
//
//  @Override
//  public void log(String tag, String message) {
//    if (logLevel >= LOG_INFO) getApplicationLogger().log(tag, message);
//  }
//
//  @Override
//  public void log(String tag, String message, Throwable exception) {
//    if (logLevel >= LOG_INFO) getApplicationLogger().log(tag, message, exception);
//  }
//
//  @Override
//  public void error(String tag, String message) {
//    if (logLevel >= LOG_ERROR) getApplicationLogger().error(tag, message);
//  }
//
//  @Override
//  public void error(String tag, String message, Throwable exception) {
//    if (logLevel >= LOG_ERROR) getApplicationLogger().error(tag, message, exception);
//  }
//
//  @Override
//  public void debug(String tag, String message) {
//    if (logLevel >= LOG_DEBUG) getApplicationLogger().debug(tag, message);
//  }
//
//  @Override
//  public void debug(String tag, String message, Throwable exception) {
//    if (logLevel >= LOG_DEBUG) getApplicationLogger().debug(tag, message, exception);
//  }
//
//  @Override
//  public int getLogLevel() {
//    return logLevel;
//  }
//
//  @Override
//  public void setLogLevel(int logLevel) {
//    this.logLevel = logLevel;
//  }
//
//  @Override
//  public ApplicationLogger getApplicationLogger() {
//    return applicationLogger;
//  }
//
//  @Override
//  public void setApplicationLogger(ApplicationLogger applicationLogger) {
//    this.applicationLogger = applicationLogger;
//  }
//
//  @Override
//  public ApplicationType getType() {
//    return ApplicationType.HeadlessDesktop;
//  }
//
//  @Override
//  public int getVersion() {
//    return 0;
//  }
//
//  @Override
//  public long getJavaHeap() {
//    return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//  }
//
//  @Override
//  public long getNativeHeap() {
//    return getJavaHeap();
//  }
//
//  @Override
//  public Preferences getPreferences(String name) {
//    if (!preferences.containsKey(name))
//      preferences.put(name, new HeadlessPreferences(name, ".prefs/"));
//
//    return preferences.get(name);
//  }
//
//  @Override
//  public Clipboard getClipboard() {
//    return null;
//  }
//
//  @Override
//  public void postRunnable(Runnable runnable) {
//    synchronized (runnables) {
//      runnables.add(runnable);
//    }
//  }
//
//  @Override
//  public void exit() {
//    synchronized (lifecycleListeners) {
//      for (final LifecycleListener lifecycleListener : new Array.ArrayIterator<>(lifecycleListeners)) {
//        lifecycleListener.pause();
//        lifecycleListener.dispose();
//      }
//    }
//
//    window.dispose();
//
//    glfwTerminate();
//
//    running = false;
//  }
//
//  @Override
//  public void addLifecycleListener(LifecycleListener listener) {
//    synchronized (lifecycleListeners) {
//      lifecycleListeners.add(listener);
//    }
//  }
//
//  @Override
//  public void removeLifecycleListener(LifecycleListener listener) {
//    synchronized (lifecycleListeners) {
//      lifecycleListeners.removeValue(listener, true);
//    }
//  }
//}
