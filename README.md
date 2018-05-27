# SpoRenderer
Android OpenGL video renderer based on GPUImage, providing color blind correction filters.

# Sample Usage
1. init GLSurfaceView
```
 mPlayerView = new GLSurfaceView(this);
 mPlayerView.getHolder().addCallback(this);
 mPlayerView.setEGLContextFactory(new ContextFactory());
 mPlayerView.setEGLConfigChooser(new ConfigChooser());
 
 spoRenderer = new SpoRenderer(mPlayerView);
 mPlayerView.setRenderer(spoRenderer);
```
 
2. init player
```
 player = new MediaPlayer();
 player.setDataSource(url);
 player.prepare();
 spoRenderer.setPlayer(player);
 mPlayerView.onResume();
```
3. set filter or filter intensity
```
spoRenderer.setGlFilter(new GlFilter());
```
  or
```
spoRenderer.setGlFilter(new GlRedBlindFilter(5))
```
  or
```
spoRenderer.setGlFilterIntensity(8)
```
  
4. release
```
   @Override
public void surfaceDestroyed(SurfaceHolder holder) {
   ...
   spoRenderer.release();
　...
}
``` 
 
# Filters  
Protanope and Protanomaly Correction
```
spoRenderer.setGlFilter(new GlRedBlindFilter(intensity));
```
  
Deuteranopia and Deuteranomaly Correction
```
spoRenderer.setGlFilter(new GlGreenBlindFilter(intensity));
```
  
Tritanopia and Tritanomaly Correction
```
spoRenderer.setGlFilter(new GlBlueBlindFilter(intensity));
```

# Refer
https://blog.csdn.net/AVLabs
