package com.axlecho.jtsviewer;

import com.axlecho.jtsviewer.untils.JtsTextUnitls;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextUnitTest {
    @Test
    public void versionCompare() throws Exception {
        assertEquals(0, JtsTextUnitls.compareVersion("v0.0.2", "0.0.2"));
        assertEquals(1, JtsTextUnitls.compareVersion("v0.0.3", "0.0.2"));
        assertEquals(-1, JtsTextUnitls.compareVersion("v0.0.2", "0.0.3"));
        assertEquals(-2, JtsTextUnitls.compareVersion("v0.0.2", "v0.0.4"));
        assertEquals(1, JtsTextUnitls.compareVersion("v0.0.2", "v0.0.1-preview"));
        assertEquals(0, JtsTextUnitls.compareVersion("v0.0.1a", "v0.0.1-preview"));
        assertEquals(0, JtsTextUnitls.compareVersion("v0.0.1a", "v0.0.1c"));
        assertEquals(-1, JtsTextUnitls.compareVersion("v0.0.1a", "v0.10.1c"));
    }

    @Test
    public void sizeFormat() throws Exception {
        assertEquals("3.10 MB", JtsTextUnitls.sizeFormat(3250585L));
        assertEquals("0 B", JtsTextUnitls.sizeFormat(0L));
        assertEquals("1023 B", JtsTextUnitls.sizeFormat(1023L));
        assertEquals("1.00 KB", JtsTextUnitls.sizeFormat(1024L));
        // assertEquals("1.00 KB",JtsTextUnitls.sizeFormat(-1));
    }
}