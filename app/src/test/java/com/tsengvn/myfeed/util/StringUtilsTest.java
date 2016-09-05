package com.tsengvn.myfeed.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * @author : hienngo
 * @since : Sep 05, 2016.
 */
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class StringUtilsTest {
    @Test
    public void testIsEmpty() throws Exception{
        Assert.assertTrue(StringUtils.isEmpty(""));
        Assert.assertTrue(StringUtils.isEmpty(" "));
        Assert.assertTrue(StringUtils.isEmpty("   "));
        Assert.assertTrue(StringUtils.isEmpty(null));
        Assert.assertTrue(StringUtils.isEmpty(null));

        Assert.assertFalse(StringUtils.isEmpty("abc"));
        Assert.assertFalse(StringUtils.isEmpty(" abc"));
        Assert.assertFalse(StringUtils.isEmpty(" abc   "));
        Assert.assertFalse(StringUtils.isEmpty("   abc   "));
    }

    @Test
    public void testTrim() throws Exception{
        Assert.assertEquals("abc", StringUtils.trim("abc"));
        Assert.assertEquals("abc", StringUtils.trim(" abc"));
        Assert.assertEquals("abc", StringUtils.trim("    abc"));
        Assert.assertEquals("abc", StringUtils.trim("    abc "));
        Assert.assertEquals("abc", StringUtils.trim("    abc    "));
        Assert.assertEquals("", StringUtils.trim(""));
        Assert.assertEquals(null, null);
    }


}