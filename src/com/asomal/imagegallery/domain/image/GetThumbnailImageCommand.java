package com.asomal.imagegallery.domain.image;

import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;

import com.asomal.imagegallery.infrastructure.Command;
import com.asomal.imagegallery.util.Logger;

public class GetThumbnailImageCommand implements Command<Bitmap> {

	private static final String TAG = GetThumbnailImageCommand.class.getSimpleName();

	String filePath;
	Context context;
	private ImageCache cache;

	public GetThumbnailImageCommand(Context context, String filePath, ImageCache cache) {
		this.context = context;
		this.filePath = filePath;
		this.cache = cache;
	}

	@Override
	public Bitmap execute() {
		return cache.getImage(filePath);
	}

	@Override
	public void onCanceled() {
		String fileName = new StringBuilder(filePath).substring(1);
		try {
			if (context.openFileInput(fileName) != null) {
				return;
			}

			// ファイルがキャンセルなどで中途半端に書き込まれているとnullになる？ので一旦消す
			context.deleteFile(fileName);
		} catch (FileNotFoundException e) {
			Logger.d(TAG, "Cancel file is not created.");
		}
	}
}
