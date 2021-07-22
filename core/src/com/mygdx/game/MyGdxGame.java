package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Sprite sprite;
	Texture img;
	OrthographicCamera camera;
	ExtendViewport viewport;
	float viewportWidth, viewportHeight;
	int Size_Width = 256;
	int Size_Height=256;

	@Override
	public void create () {

		viewportWidth = Gdx.app.getGraphics().getWidth();
		viewportHeight = Gdx.app.getGraphics().getHeight();
		batch = new SpriteBatch();
		sprite = new Sprite();
		img = new Texture("badlogic.jpg");
		sprite = new Sprite(img, (int)0,(int)0,(int)Size_Width,(int)Size_Height);
		sprite.setPosition(viewportWidth/2-Size_Width/2, viewportHeight/2-Size_Height/2);
		camera = new OrthographicCamera();

		viewport = new ExtendViewport(1000,1000,camera);
		viewport.apply();
		camera.zoom = 1.0F;
	}

	@Override
	public void render () {
		// camera 업데이트
		camera.update();

		// 배경 그리기
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);

		// 이미지 그리기
		batch.begin();
		sprite.draw(batch);
		batch.end();
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	@Override
	public void resize(int width, int height)
	{
		// viewport 업데이트
		viewport.update(width,height);
		// camera 업데이트
		camera.position.set(viewportWidth/2-100,viewportHeight/2-100,0);
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
