package com.project.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter  {

	SpriteBatch batch;
	Texture background;
	int max=0;

	Texture gameover;
 	Music music;
	Sound wing;
 	Music sound2;
	Texture[] birds;
	int flapState = 0;
	float birdY = 0;
	float velocity = 0;
	Circle birdCircle;
	int score = 0;
	int scoringTube = 0;
	BitmapFont font;
	BitmapFont font2;
	BitmapFont str;
	BitmapFont str2;
	BitmapFont maxscore;
	BitmapFont highscore;


	int gameState = 0;
	float gravity = 2;

	Texture topTube;
	Texture bottomTube;
	float gap = 500;
	float maxTubeOffset;
	Random randomGenerator;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameover = new Texture("gameover.png");

		birdCircle = new Circle();
		font = new BitmapFont(Gdx.files.internal("font1.fnt"));
		font.setColor(Color.WHITE);
		font.getData().setScale(2);
		font2 = new BitmapFont(Gdx.files.internal("font1.fnt"));
		font2.setColor(Color.WHITE);
		font2.getData().setScale(2);
		str = new BitmapFont(Gdx.files.internal("font0.fnt"));
		str.setColor(Color.NAVY);
		str.getData().setScale(1);
		str2 = new BitmapFont(Gdx.files.internal("font0.fnt"));
		str2.setColor(Color.BROWN);
		str2.getData().setScale(1);
		maxscore = new BitmapFont(Gdx.files.internal("font1.fnt"));
		maxscore.setColor(Color.RED);
		maxscore.getData().setScale(2);
		highscore = new BitmapFont(Gdx.files.internal("font1.fnt"));
		highscore.setColor(Color.RED);
		highscore.getData().setScale(2);

		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];
		startGame();

	}

	public void startGame() {
		music= Gdx.audio.newMusic(Gdx.files.internal("song.mp3"));
		music.setLooping(true);
		music.setVolume(.2f);
		music.play();
		wing= Gdx.audio.newSound(Gdx.files.internal("wing.ogg"));


		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

		for (int i = 0; i < numberOfTubes; i++) {

			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		if (gameState == 1) {


			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
				score++;
				if (max<score)
				{
					max=score;

				}


				Gdx.app.log("Score", String.valueOf(score));
				Gdx.app.log("MAXScore", String.valueOf(max));



				if (scoringTube < numberOfTubes - 1) {

					scoringTube++;
				} else {

					scoringTube = 0;

				}
			}
			if (Gdx.input.justTouched()) {

				velocity = -20;
				wing.play(.2f);

			}
			for (int i = 0; i < numberOfTubes; i++) {
				if (tubeX[i] < - topTube.getWidth()) {
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				} else {
					tubeX[i] = tubeX[i] - tubeVelocity;
				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			}

			if (birdY > 0) {

				velocity = velocity + gravity;
				birdY -= velocity;

			} else {
				gameState = 2;
			}

		} else if (gameState == 0) {
			str.draw(batch, "Touch Anywhere to Play", 380,850);

			if (Gdx.input.justTouched()) {

				gameState = 1;
				str.dispose();

			}

		} else if (gameState == 2) {

			batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);
			music.dispose();
			sound2.dispose();
			str2.draw(batch, "Touch Anywhere to Play Again", 330,850);
			if (Gdx.input.justTouched()) {

				gameState = 1;

				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
				if (score==max)
				{
					max=score;
				}
			}
		}
		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}
		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
		font.draw(batch, String.valueOf(score), 5, 100);
		font2.draw(batch, "SCORE", 5,150 );
		maxscore.draw(batch, String.valueOf(max),Gdx.graphics.getWidth()-250,100);
		highscore.draw(batch, "High Score",Gdx.graphics.getWidth()-250,150);
		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);
		for (int i = 0; i < numberOfTubes; i++) {
			if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
				gameState = 2;
				sound2= Gdx.audio.newMusic(Gdx.files.internal("sound2.wav"));
				sound2.setVolume(100f);
				sound2.play();
			}
		}
		batch.end();
	}

}
