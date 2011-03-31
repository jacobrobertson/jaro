package com.robestone.jaro.piecerules;

import com.robestone.jaro.Piece;
import com.robestone.jaro.SpriteMapper;

public class CaveRules extends PieceRulesAdapter {

	public static final String CAVE_TYPE_ID = "cave";
	
	private static final String cave_bottom = "cave_bottom";
	private static final String cave_left = "cave_left";
	private static final String cave_right = "cave_right";
	private static final String cave_top = "cave_top";
	
	private static final String cave_t_center = "cave_t_center";
	
	private static final String cave_t_bottom = "cave_t_bottom";
	private static final String cave_t_left = "cave_t_left";
	private static final String cave_t_right = "cave_t_right";
	private static final String cave_t_top = "cave_t_top";
	
	private static final String cave_wall_t_bottom = "cave_wall_t_bottom";
	private static final String cave_wall_t_left = "cave_wall_t_left";
	private static final String cave_wall_t_right = "cave_wall_t_right";
	private static final String cave_wall_t_top = "cave_wall_t_top";
	
	private static final String cave_solid = "cave_solid";
	private static final String cave_bottom_left = "cave_bottom_left";
	private static final String cave_bottom_right = "cave_bottom_right";
	private static final String cave_top_left = "cave_top_left";
	private static final String cave_top_right = "cave_top_right";
	private static final String cave_bottom_left_bump = "cave_bottom_left_bump";
	private static final String cave_bottom_right_bump = "cave_bottom_right_bump";
	private static final String cave_top_left_bump = "cave_top_left_bump";
	private static final String cave_top_right_bump = "cave_top_right_bump";
	private static final String cave_bottom_left_L = "cave_bottom_left_l";
	private static final String cave_bottom_right_L = "cave_bottom_right_l";
	private static final String cave_top_left_L = "cave_top_left_l";
	private static final String cave_top_right_L = "cave_top_right_l";
	private static final String cave_wall_right_bump = "cave_wall_right_bump";
	private static final String cave_wall_left_bump = "cave_wall_left_bump";
	private static final String cave_wall_top_bump = "cave_wall_top_bump";
	private static final String cave_wall_bottom_bump = "cave_wall_bottom_bump";
	private static final String cave_wall_vertical = "cave_wall_vertical";
	private static final String cave_wall_horizontal = "cave_wall_horizontal";
	
	private static final String cave_bottom_bl_corner = "cave_bottom_bl_corner";
	private static final String cave_bottom_br_corner = "cave_bottom_br_corner";
	private static final String cave_right_br_corner = "cave_right_br_corner";
	private static final String cave_right_tr_corner = "cave_right_tr_corner";
	private static final String cave_top_tl_corner = "cave_top_tl_corner";
	private static final String cave_top_tr_corner = "cave_top_tr_corner";
	private static final String cave_left_bl_corner = "cave_left_bl_corner";
	private static final String cave_left_tl_corner = "cave_left_tl_corner";
	private static final String cave_diag_a = "cave_diag_a";
	private static final String cave_diag_b = "cave_diag_b";

	private SpriteMapper map;
	
	public CaveRules() {
		super(CAVE_TYPE_ID);
		setBlockingStateId(null);
		map = buildSpriteMapper();
		
	}
	@Override
	protected String doGetSpriteId(Piece piece, boolean isLandscape) {
		return map.getSpriteId(piece, isLandscape);
	}

	private SpriteMapper buildSpriteMapper() {
		SpriteMapper map = new SpriteMapper();
 		map.addMatch(CAVE_TYPE_ID, cave_bottom, cave_bottom, cave_right);
		map.addMatch(CAVE_TYPE_ID, cave_left, cave_left, cave_bottom);
		map.addMatch(CAVE_TYPE_ID, cave_right, cave_right, cave_top);
		map.addMatch(CAVE_TYPE_ID, cave_top, cave_top, cave_left);
		map.addMatch(CAVE_TYPE_ID, cave_solid, cave_solid, cave_solid);
		
		map.addMatch(CAVE_TYPE_ID, cave_t_bottom, cave_t_bottom, cave_t_right);
		map.addMatch(CAVE_TYPE_ID, cave_t_left, cave_t_left, cave_t_bottom);
		map.addMatch(CAVE_TYPE_ID, cave_t_right, cave_t_right, cave_t_top);
		map.addMatch(CAVE_TYPE_ID, cave_t_top, cave_t_top, cave_t_left);
		map.addMatch(CAVE_TYPE_ID, cave_t_center, cave_t_center, cave_t_center);

		map.addMatch(CAVE_TYPE_ID, cave_wall_t_bottom, cave_wall_t_bottom, cave_wall_t_right);
		map.addMatch(CAVE_TYPE_ID, cave_wall_t_left, cave_wall_t_left, cave_wall_t_bottom);
		map.addMatch(CAVE_TYPE_ID, cave_wall_t_right, cave_wall_t_right, cave_wall_t_top);
		map.addMatch(CAVE_TYPE_ID, cave_wall_t_top, cave_wall_t_top, cave_wall_t_left);

		map.addMatch(CAVE_TYPE_ID, cave_bottom_left, cave_bottom_left, cave_bottom_right);
		map.addMatch(CAVE_TYPE_ID, cave_bottom_right, cave_bottom_right, cave_top_right);
		map.addMatch(CAVE_TYPE_ID, cave_top_left, cave_top_left, cave_bottom_left);
		map.addMatch(CAVE_TYPE_ID, cave_top_right, cave_top_right, cave_top_left);
		map.addMatch(CAVE_TYPE_ID, cave_bottom_left_bump, cave_bottom_left_bump, cave_bottom_right_bump);
		map.addMatch(CAVE_TYPE_ID, cave_bottom_right_bump, cave_bottom_right_bump, cave_top_right_bump);
		map.addMatch(CAVE_TYPE_ID, cave_top_left_bump, cave_top_left_bump, cave_bottom_left_bump);
		map.addMatch(CAVE_TYPE_ID, cave_top_right_bump, cave_top_right_bump, cave_top_left_bump);

		map.addMatch(CAVE_TYPE_ID, cave_bottom_left_L, cave_bottom_left_L, cave_bottom_right_L);
		map.addMatch(CAVE_TYPE_ID, cave_bottom_right_L, cave_bottom_right_L, cave_top_right_L);
		map.addMatch(CAVE_TYPE_ID, cave_top_left_L, cave_top_left_L, cave_bottom_left_L);
		map.addMatch(CAVE_TYPE_ID, cave_top_right_L, cave_top_right_L, cave_top_left_L);

		map.addMatch(CAVE_TYPE_ID, cave_wall_right_bump, cave_wall_right_bump, cave_wall_top_bump);
		map.addMatch(CAVE_TYPE_ID, cave_wall_left_bump, cave_wall_left_bump, cave_wall_bottom_bump);
		map.addMatch(CAVE_TYPE_ID, cave_wall_top_bump, cave_wall_top_bump, cave_wall_left_bump);
		map.addMatch(CAVE_TYPE_ID, cave_wall_bottom_bump, cave_wall_bottom_bump, cave_wall_right_bump);

		map.addMatch(CAVE_TYPE_ID, cave_wall_vertical, cave_wall_vertical, cave_wall_horizontal);
		map.addMatch(CAVE_TYPE_ID, cave_wall_horizontal, cave_wall_horizontal, cave_wall_vertical);
		
		
		map.addMatch(CAVE_TYPE_ID, cave_bottom_bl_corner, cave_bottom_bl_corner, cave_right_br_corner);
		map.addMatch(CAVE_TYPE_ID, cave_bottom_br_corner, cave_bottom_br_corner, cave_right_tr_corner);
		map.addMatch(CAVE_TYPE_ID, cave_left_bl_corner, cave_left_bl_corner, cave_bottom_br_corner);
		map.addMatch(CAVE_TYPE_ID, cave_left_tl_corner, cave_left_tl_corner, cave_bottom_bl_corner);
		map.addMatch(CAVE_TYPE_ID, cave_right_br_corner, cave_right_br_corner, cave_top_tr_corner);
		map.addMatch(CAVE_TYPE_ID, cave_right_tr_corner, cave_right_tr_corner, cave_top_tl_corner);
		map.addMatch(CAVE_TYPE_ID, cave_top_tl_corner, cave_top_tl_corner, cave_left_bl_corner);
		map.addMatch(CAVE_TYPE_ID, cave_top_tr_corner, cave_top_tr_corner, cave_left_tl_corner);
		
		map.addMatch(CAVE_TYPE_ID, cave_diag_a, cave_diag_a, cave_diag_b);
		map.addMatch(CAVE_TYPE_ID, cave_diag_b, cave_diag_b, cave_diag_a);
		
		return map;
	}
	@Override
	protected Piece buildPiece(String type, String state) {
		return new Piece(type, state, null);
	}

}
