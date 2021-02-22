package com.example.android.glass.glassdesign.agora;

import java.nio.ByteBuffer;

import io.agora.rtc.mediaio.IVideoSink;
import io.agora.rtc.mediaio.MediaIO;

public class AgoraVideoRender implements IVideoSink {
    private com.example.android.glass.glassdesign.agora.Peer mPeer;
    private boolean mIsLocal;

    public AgoraVideoRender(int uid, boolean local) {
        mPeer = new com.example.android.glass.glassdesign.agora.Peer();
        mPeer.uid = uid;
        mIsLocal = local;
    }

    public com.example.android.glass.glassdesign.agora.Peer getPeer() {
        return mPeer;
    }

    @Override
    public boolean onInitialize() {
        return true;
    }

    @Override
    public boolean onStart() {
        return true;
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDispose() {

    }

    @Override
    public long getEGLContextHandle() {
        return 0;
    }

    @Override
    public int getBufferType() {
        return MediaIO.BufferType.BYTE_BUFFER.intValue();
    }

    @Override
    public int getPixelFormat() {
        return MediaIO.PixelFormat.RGBA.intValue();
    }

    @Override
    public void consumeByteBufferFrame(ByteBuffer buffer, int format, int width, int height, int rotation, long ts) {
        if (!mIsLocal) {
            mPeer.data = buffer;
            mPeer.width = width;
            mPeer.height = height;
            mPeer.rotation = rotation;
            mPeer.ts = ts;
        }
    }

    @Override
    public void consumeByteArrayFrame(byte[] data, int format, int width, int height, int rotation, long ts) {
        //Log.e("AgoraVideoRender", "consumeByteArrayFrame");
    }

    @Override
    public void consumeTextureFrame(int texId, int format, int width, int height, int rotation, long ts, float[] matrix) {

    }

}