package com.npdevs.healthcastle.predictivemodels;

public interface Classifier {
	String name();

	Classification recognize(final float[] pixels);
}
