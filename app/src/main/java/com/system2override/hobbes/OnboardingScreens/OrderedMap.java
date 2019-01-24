package com.system2override.hobbes.OnboardingScreens;

public class OrderedMap {
    public static Class returnClass(int i) {
        switch (i) {
            case 0: return WelcomeScreen.class;
        }
        return WelcomeScreen.class;
    }
}
