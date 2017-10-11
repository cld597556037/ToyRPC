package com.dong.rpc.extension;

import com.dong.rpc.exception.ToyException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author caolidong
 * @date 2017/9/28.
 */
public class ExtensionLoaderTest {

    ExtensionLoader<SpiInterface> loader;

    @Before
    public void init() {
        loader = ExtensionLoader.getExtensionLoader(SpiInterface.class);
    }


    @Test
    public void testGetExtension() {
        try {
            SpiInterface impl = loader.getExtension("impl");
            assert impl instanceof SpiImpl;
            SpiInterface impl1 = loader.getExtension("impl1");
            assert impl1 instanceof SpiImpl1;
        } catch (ToyException e) {
            e.printStackTrace();
        }
    }
}
