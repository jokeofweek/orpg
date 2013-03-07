package orpg.client.systems;

import orpg.client.BaseClient;
import orpg.client.data.component.AnimatedPlayer;
import orpg.client.ui.ViewBox;
import orpg.shared.Constants;
import orpg.shared.data.component.Moveable;
import orpg.shared.data.component.Position;
import orpg.shared.data.component.Renderable;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderSystem extends EntityProcessingSystem {
	@Mapper
	ComponentMapper<Position> positionMapper;
	@Mapper
	ComponentMapper<Renderable> renderableMapper;
	@Mapper
	ComponentMapper<Moveable> moveableMapper;
	@Mapper
	ComponentMapper<AnimatedPlayer> animationMapper;

	private BaseClient baseClient;

	private ViewBox viewBox;
	private Texture[] spriteSets;
	private SpriteBatch spriteBatch;

	private int dX;
	private int dY;
	private int startX;
	private int startY;
	private int endX;
	private int endY;

	public RenderSystem(BaseClient baseClient) {
		super(Aspect.getAspectForAll(Position.class, Renderable.class));
		this.baseClient = baseClient;
	}

	public void prepare(ViewBox viewBox, Texture[] spriteSets,
			SpriteBatch spriteBatch) {
		this.viewBox = viewBox;
		this.spriteSets = spriteSets;
		this.spriteBatch = spriteBatch;

		startX = viewBox.getStartX();
		startY = viewBox.getStartY();
		endX = viewBox.getEndX();
		endY = viewBox.getEndY();
		dX = viewBox.getOffsetX() % Constants.TILE_WIDTH;
		dY = viewBox.getOffsetY() % Constants.TILE_HEIGHT;

	}

	@Override
	protected void process(Entity e) {
		Position position = positionMapper.get(e);

		if (position.getX() < startX || position.getX() > endX
				|| position.getY() < startY || position.getY() > endY) {
			return;
		}

		Renderable renderable = renderableMapper.get(e);
		Moveable moveable = moveableMapper.get(e);
		AnimatedPlayer animation = animationMapper.getSafe(e);

		// Render the character's texture
		int sprite = renderable.getRenderReference();
		int spriteSet = sprite / Constants.SPRITES_PER_SPRITESET;
		int spriteX = sprite % Constants.SPRITESET_WIDTH;
		int spriteY = (sprite % Constants.SPRITES_PER_SPRITESET)
				/ Constants.SPRITESET_WIDTH;

		// Calculate the frame offset
		int frame = 0;
		if (animation != null) {
			frame = animation.getFrame();
		}

		spriteBatch
				.draw(spriteSets[spriteSet],
						((position.getX() - startX) * Constants.TILE_WIDTH)
								- dX + animation.getXOffset(),
						((position.getY() - startY) * Constants.TILE_WIDTH)
								- dY + animation.getYOffset(),
						Constants.SPRITE_FRAME_WIDTH,
						Constants.SPRITE_FRAME_HEIGHT,
						(spriteX * Constants.SPRITE_WIDTH)
								+ (frame * Constants.SPRITE_FRAME_WIDTH),
						(spriteY * Constants.SPRITE_HEIGHT)
								+ (moveable.getDirection().ordinal() * Constants.SPRITE_FRAME_HEIGHT),
						Constants.SPRITE_FRAME_WIDTH,
						Constants.SPRITE_FRAME_HEIGHT, false, true);
	}

}
