package com.system2override.yoke;

import com.system2override.yoke.Utilities.RandomUtilities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;

public class RandomUtilitiesTest {

    @Test
    public void testFormatting() {
        System.out.println(RandomUtilities.formatMillisecondsToMinutes(0));
    }
}
