package com.loment.cashewnut.activity.list;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.loment.cashewnut.AppConfiguration;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Emoji {
	static Emoji instance = null;
	public static LinkedHashMap<String, Image> sEmojisMap = new LinkedHashMap();
	public static LinkedHashMap<String, Image> RecentsEmojisMap = new LinkedHashMap();

	public static Emoji getInstance() {
		if (instance == null) {
			instance = new Emoji();
		}
		return instance;
	}

	static {
		sEmojisMap.put(new String(Character.toChars(0x1f600)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f600.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f601)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f601.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f602)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f602.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f604)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f604.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f603)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f603.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f60a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f60a.png")));
		sEmojisMap.put(new String(Character.toChars(0x263a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_263a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f609)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f609.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f60d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f60d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f618)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f618.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f61a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f61a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f617)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f617.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f619)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f619.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f61c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f61c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f61d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f61d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f61b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f61b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f633)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f633.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f614)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f614.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f60c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f60c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f612)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f612.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f61e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f61e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f623)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f623.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f622)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f622.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f62d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f62d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f62a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f62a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f625)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f625.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f630)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f630.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f605)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f605.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f613)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f613.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f629)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f629.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f62b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f62b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f628)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f628.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f631)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f631.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f620)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f620.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f621)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f621.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f624)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f624.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f616)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f616.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f606)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f606.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f60b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f60b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f637)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f637.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f60e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f60e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f634)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f634.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f635)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f635.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f632)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f632.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f61f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f61f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f626)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f626.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f627)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f627.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f608)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f608.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f47f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f47f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f62e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f62e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f62c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f62c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f610)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f610.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f615)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f615.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f62f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f62f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f636)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f636.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f607)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f607.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f60f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f60f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f611)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f611.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f472)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f472.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f473)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f473.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f46e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f46e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f477)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f477.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f482)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f482.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f476)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f476.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f466)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f466.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f467)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f467.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f468)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f468.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f469)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f469.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f474)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f474.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f475)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f475.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f471)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f471.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f47c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f47c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f478)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f478.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f63a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f63a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f638)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f638.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f63b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f63b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f63d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f63d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f63c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f63c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f640)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f640.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f63f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f63f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f639)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f639.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f63e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f63e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f479)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f479.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f47a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f47a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f648)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f648.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f649)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f649.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f64a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f64a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f480)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f480.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f47d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f47d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4a9)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4a9.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f525)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f525.png")));
		sEmojisMap.put(new String(Character.toChars(0x2728)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2728.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f31f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f31f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4ab)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4ab.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4a5)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4a5.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4a2)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4a2.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4a6)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4a6.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4a7)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4a7.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4a4)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4a4.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4a8)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4a8.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f442)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f442.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f440)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f440.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f443)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f443.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f445)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f445.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f444)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f444.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f44d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f44d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f44e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f44e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f44c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f44c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f44a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f44a.png")));
		sEmojisMap.put(new String(Character.toChars(0x270a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_270a.png")));
		/*sEmojisMap.put(new String(Character.toChars(0x270c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_270c.png")));*/
		sEmojisMap.put(new String(Character.toChars(0x1f44b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f44b.png")));
		sEmojisMap.put(new String(Character.toChars(0x270b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_270b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f450)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f450.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f446)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f446.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f447)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f447.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f449)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f449.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f448)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f448.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f64c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f64c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f64f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f64f.png")));
		sEmojisMap.put(new String(Character.toChars(0x261d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_261d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f44f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f44f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4aa)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4aa.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6b6)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6b6.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3c3)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3c3.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f483)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f483.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f46b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f46b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f46a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f46a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f46c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f46c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f46d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f46d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f48f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f48f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f491)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f491.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f46f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f46f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f646)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f646.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f645)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f645.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f481)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f481.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f64b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f64b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f486)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f486.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f487)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f487.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f485)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f485.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f470)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f470.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f64e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f64e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f64d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f64d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f647)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f647.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3a9)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3a9.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f451)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f451.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f452)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f452.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f45f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f45f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f45e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f45e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f461)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f461.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f460)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f460.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f462)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f462.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f455)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f455.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f454)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f454.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f45a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f45a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f457)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f457.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3bd)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3bd.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f456)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f456.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f458)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f458.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f459)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f459.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4bc)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4bc.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f45c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f45c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f45d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f45d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f45b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f45b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f453)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f453.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f380)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f380.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f302)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f302.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f484)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f484.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f49b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f49b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f499)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f499.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f49c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f49c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f49a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f49a.png")));
		sEmojisMap.put(new String(Character.toChars(0x2764)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2764.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f494)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f494.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f497)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f497.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f493)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f493.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f495)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f495.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f496)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f496.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f49e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f49e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f498)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f498.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f48c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f48c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f48b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f48b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f48d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f48d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f48e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f48e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f464)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f464.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f465)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f465.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4ac)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4ac.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f463)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f463.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4ad)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4ad.png")));

		// Nature
		sEmojisMap.put(new String(Character.toChars(0x1f436)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f436.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f43a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f43a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f431)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f431.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f42d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f42d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f439)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f439.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f430)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f430.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f438)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f438.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f42f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f42f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f428)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f428.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f43b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f43b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f437)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f437.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f43d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f43d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f42e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f42e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f417)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f417.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f435)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f435.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f412)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f412.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f434)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f434.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f411)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f411.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f418)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f418.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f43c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f43c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f427)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f427.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f426)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f426.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f424)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f424.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f425)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f425.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f423)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f423.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f414)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f414.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f40d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f40d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f422)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f422.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f41b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f41b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f41d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f41d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f41c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f41c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f41e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f41e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f40c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f40c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f419)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f419.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f41a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f41a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f420)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f420.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f41f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f41f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f42c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f42c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f433)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f433.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f40b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f40b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f404)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f404.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f40f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f40f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f400)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f400.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f403)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f403.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f405)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f405.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f407)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f407.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f409)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f409.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f40e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f40e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f410)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f410.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f413)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f413.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f415)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f415.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f416)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f416.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f401)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f401.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f402)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f402.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f432)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f432.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f421)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f421.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f40a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f40a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f42b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f42b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f42a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f42a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f406)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f406.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f408)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f408.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f429)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f429.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f43e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f43e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f490)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f490.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f338)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f338.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f337)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f337.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f340)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f340.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f339)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f339.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f33b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f33b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f33a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f33a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f341)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f341.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f343)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f343.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f342)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f342.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f33f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f33f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f33e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f33e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f344)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f344.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f335)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f335.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f334)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f334.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f332)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f332.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f333)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f333.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f330)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f330.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f331)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f331.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f33c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f33c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f310)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f310.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f31e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f31e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f31d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f31d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f31a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f31a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f311)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f311.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f312)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f312.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f313)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f313.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f314)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f314.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f315)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f315.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f316)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f316.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f317)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f317.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f318)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f318.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f31c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f31c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f31b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f31b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f319)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f319.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f30d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f30d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f30e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f30e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f30f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f30f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f30b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f30b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f30c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f30c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f320)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f303.png"))); // TODO
																													// (rockerhieu)
																													// review
																													// this
																													// emoji
		sEmojisMap.put(new String(Character.toChars(0x2b50)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2b50.png")));
		sEmojisMap.put(new String(Character.toChars(0x2600)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2600.png")));
		sEmojisMap.put(new String(Character.toChars(0x26c5)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_26c5.png")));
		sEmojisMap.put(new String(Character.toChars(0x2601)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2601.png")));
		sEmojisMap.put(new String(Character.toChars(0x26a1)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_26a1.png")));
		sEmojisMap.put(new String(Character.toChars(0x2614)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2614.png")));
		sEmojisMap.put(new String(Character.toChars(0x2744)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2744.png")));
		sEmojisMap.put(new String(Character.toChars(0x26c4)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_26c4.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f300)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f300.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f301)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f301.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f308)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f308.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f30a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f30a.png")));

		// Objects
		sEmojisMap.put(new String(Character.toChars(0x1f38d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f38d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f49d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f49d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f38e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f38e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f392)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f392.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f393)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f393.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f38f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f38f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f386)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f386.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f387)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f387.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f390)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f390.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f391)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f391.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f383)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f383.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f47b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f47b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f385)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f385.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f384)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f384.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f381)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f381.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f38b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f38b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f389)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f389.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f38a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f38a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f388)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f388.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f38c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f38c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f52e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f52e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3a5)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3a5.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4f7)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4f7.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4f9)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4f9.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4fc)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4fc.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4bf)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4bf.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4c0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4c0.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4bd)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4bd.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4be)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4be.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4bb)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4bb.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4f1)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4f1.png")));
		sEmojisMap.put(new String(Character.toChars(0x260e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_260e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4de)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4de.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4df)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4df.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4e0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4e0.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4e1)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4e1.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4fa)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4fa.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4fb)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4fb.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f50a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f50a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f509)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f509.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f508)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f508.png"))); // TODO
																													// (rockerhieu):
																													// review
																													// this
																													// emoji
		sEmojisMap.put(new String(Character.toChars(0x1f507)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f507.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f514)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f514.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f515)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f515.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4e2)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4e2.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4e3)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4e3.png")));
		sEmojisMap.put(new String(Character.toChars(0x23f3)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_23f3.png")));
		sEmojisMap.put(new String(Character.toChars(0x231b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_231b.png")));
		sEmojisMap.put(new String(Character.toChars(0x23f0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_23f0.png")));
		sEmojisMap.put(new String(Character.toChars(0x231a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_231a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f513)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f513.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f512)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f512.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f50f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f50f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f510)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f510.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f511)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f511.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f50e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f50e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4a1)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4a1.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f526)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f526.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f506)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f506.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f505)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f505.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f50c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f50c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f50b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f50b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f50d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f50d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6c1)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6c1.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6c0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6c0.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6bf)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6bf.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6bd)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6bd.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f527)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f527.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f529)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f529.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f528)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f528.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6aa)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6aa.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6ac)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6ac.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4a3)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4a3.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f52b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f52b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f52a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f52a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f48a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f48a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f489)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f489.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4b0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4b0.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4b4)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4b4.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4b5)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4b5.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4b7)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4b7.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4b6)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4b6.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4b3)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4b3.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4b8)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4b8.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4f2)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4f2.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4e7)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4e7.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4e5)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4e5.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4e4)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4e4.png")));
		sEmojisMap.put(new String(Character.toChars(0x2709)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2709.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4e9)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4e9.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4e8)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4e8.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4ef)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4ef.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4eb)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4eb.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4ea)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4ea.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4ec)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4ec.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4ed)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4ed.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4ee)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4ee.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4e6)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4e6.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4dd)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4dd.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4c4)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4c4.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4c3)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4c3.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4d1)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4d1.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4ca)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4ca.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4c8)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4c8.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4c9)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4c9.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4dc)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4dc.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4cb)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4cb.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4c5)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4c5.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4c6)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4c6.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4c7)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4c7.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4c1)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4c1.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4c2)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4c2.png")));
		sEmojisMap.put(new String(Character.toChars(0x2702)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2702.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4cc)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4cc.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4ce)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4ce.png")));
		sEmojisMap.put(new String(Character.toChars(0x2712)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2712.png")));
		sEmojisMap.put(new String(Character.toChars(0x270f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_270f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4cf)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4cf.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4d0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4d0.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4d5)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4d5.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4d7)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4d7.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4d8)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4d8.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4d9)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4d9.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4d3)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4d3.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4d4)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4d4.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4d2)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4d2.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4da)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4da.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4d6)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4d6.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f516)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f516.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4db)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4db.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f52c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f52c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f52d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f52d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4f0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4f0.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3a8)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3a8.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3ac)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3ac.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3a4)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3a4.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3a7)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3a7.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3bc)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3bc.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3b5)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3b5.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3b6)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3b6.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3b9)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3b9.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3bb)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3bb.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3ba)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3ba.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3b7)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3b7.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3b8)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3b8.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f47e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f47e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3ae)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3ae.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f0cf)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f0cf.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3b4)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3b4.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f004)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f004.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3b2)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3b2.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3af)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3af.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3c8)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3c8.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3c0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3c0.png")));
		sEmojisMap.put(new String(Character.toChars(0x26bd)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_26bd.png")));
		sEmojisMap.put(new String(Character.toChars(0x26be)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_26be.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3be)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3be.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3b1)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3b1.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3c9)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3c9.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3b3)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3b3.png")));
		sEmojisMap.put(new String(Character.toChars(0x26f3)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_26f3.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6b5)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6b5.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6b4)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6b4.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3c1)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3c1.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3c7)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3c7.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3c6)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3c6.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3bf)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3bf.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3c2)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3c2.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3ca)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3ca.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3c4)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3c4.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3a3)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3a3.png")));
		sEmojisMap.put(new String(Character.toChars(0x2615)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2615.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f375)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f375.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f376)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f376.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f37c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f37c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f37a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f37a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f37b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f37b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f378)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f378.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f379)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f379.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f377)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f377.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f374)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f374.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f355)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f355.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f354)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f354.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f35f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f35f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f357)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f357.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f356)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f356.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f35d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f35d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f35b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f35b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f364)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f364.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f371)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f371.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f363)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f363.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f365)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f365.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f359)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f359.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f358)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f358.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f35a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f35a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f35c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f35c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f372)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f372.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f362)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f362.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f361)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f361.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f373)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f373.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f35e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f35e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f369)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f369.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f36e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f36e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f366)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f366.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f368)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f368.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f367)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f367.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f382)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f382.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f370)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f370.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f36a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f36a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f36b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f36b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f36c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f36c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f36d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f36d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f36f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f36f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f34e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f34e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f34f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f34f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f34a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f34a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f34b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f34b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f352)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f352.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f347)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f347.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f349)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f349.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f353)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f353.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f351)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f351.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f348)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f348.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f34c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f34c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f350)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f350.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f34d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f34d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f360)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f360.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f346)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f346.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f345)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f345.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f33d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f33d.png")));

		// Places
		sEmojisMap.put(new String(Character.toChars(0x1f3e0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3e0.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3e1)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3e1.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3eb)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3eb.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3e2)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3e2.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3e3)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3e3.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3e5)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3e5.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3e6)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3e6.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3ea)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3ea.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3e9)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3e9.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3e8)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3e8.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f492)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f492.png")));
		sEmojisMap.put(new String(Character.toChars(0x26ea)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_26ea.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3ec)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3ec.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3e4)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3e4.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f307)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f307.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f306)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f306.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3ef)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3ef.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3f0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3f0.png")));
		sEmojisMap.put(new String(Character.toChars(0x26fa)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_26fa.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3ed)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3ed.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f5fc)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f5fc.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f5fe)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f5fe.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f5fb)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f5fb.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f304)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f304.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f305)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f305.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f303)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f303.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f5fd)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f5fd.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f309)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f309.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3a0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3a0.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3a1)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3a1.png")));
		sEmojisMap.put(new String(Character.toChars(0x26f2)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_26f2.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3a2)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3a2.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6a2)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6a2.png")));
		sEmojisMap.put(new String(Character.toChars(0x26f5)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_26f5.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6a4)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6a4.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6a3)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6a3.png")));
		sEmojisMap.put(new String(Character.toChars(0x2693)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2693.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f680)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f680.png")));
		sEmojisMap.put(new String(Character.toChars(0x2708)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2708.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4ba)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4ba.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f681)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f681.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f682)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f682.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f68a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f68a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f689)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f689.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f69e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f69e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f686)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f686.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f684)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f684.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f685)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f685.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f688)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f688.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f687)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f687.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f69d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f69d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f68b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f68b.png"))); // TODO
																													// (rockerhieu)
																													// review
																													// this
																													// emoji
		sEmojisMap.put(new String(Character.toChars(0x1f683)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f683.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f68e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f68e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f68c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f68c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f68d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f68d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f699)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f699.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f698)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f698.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f697)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f697.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f695)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f695.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f696)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f696.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f69b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f69b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f69a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f69a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6a8)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6a8.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f693)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f693.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f694)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f694.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f692)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f692.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f691)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f691.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f690)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f690.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6b2)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6b2.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6a1)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6a1.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f69f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f69f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6a0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6a0.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f69c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f69c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f488)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f488.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f68f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f68f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3ab)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3ab.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6a6)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6a6.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6a5)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6a5.png")));
		sEmojisMap.put(new String(Character.toChars(0x26a0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_26a0.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6a7)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6a7.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f530)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f530.png")));
		sEmojisMap.put(new String(Character.toChars(0x26fd)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_26fd.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3ee)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3ee.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3b0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3b0.png")));
		sEmojisMap.put(new String(Character.toChars(0x2668)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2668.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f5ff)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f5ff.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3aa)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3aa.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3ad)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3ad.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4cd)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4cd.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6a9)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6a9.png")));
				// Emoji.fromChars("\ud83c\uddef\ud83c\uddf5");
				// Emoji.fromChars("\ud83c\uddf0\ud83c\uddf7");
				// Emoji.fromChars("\ud83c\udde9\ud83c\uddea");
				// Emoji.fromChars("\ud83c\udde8\ud83c\uddf3");
				// Emoji.fromChars("\ud83c\uddfa\ud83c\uddf8");
				// Emoji.fromChars("\ud83c\uddeb\ud83c\uddf7");
				// Emoji.fromChars("\ud83c\uddea\ud83c\uddf8");
				// Emoji.fromChars("\ud83c\uddee\ud83c\uddf9");
				// Emoji.fromChars("\ud83c\uddf7\ud83c\uddfa");
				// Emoji.fromChars("\ud83c\uddec\ud83c\udde7");

		// Symbols
		// Emoji.fromChars("\u0031\u20e3"),
		// Emoji.fromChars("\u0032\u20e3"),
		// Emoji.fromChars("\u0033\u20e3"),
		// Emoji.fromChars("\u0034\u20e3"),
		// Emoji.fromChars("\u0035\u20e3"),
		// Emoji.fromChars("\u0036\u20e3"),
		// Emoji.fromChars("\u0037\u20e3"),
		// Emoji.fromChars("\u0038\u20e3"),
		// Emoji.fromChars("\u0039\u20e3"),
		// Emoji.fromChars("\u0030\u20e3"),
		sEmojisMap.put(new String(Character.toChars(0x1f51f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f51f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f522)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f522.png")));
		// Emoji.fromChars("\u0023\u20e3"),
		sEmojisMap.put(new String(Character.toChars(0x1f523)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f523.png")));
		sEmojisMap.put(new String(Character.toChars(0x2b06)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2b06.png")));
		sEmojisMap.put(new String(Character.toChars(0x2b07)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2b07.png")));
		sEmojisMap.put(new String(Character.toChars(0x2b05)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2b05.png")));
		sEmojisMap.put(new String(Character.toChars(0x27a1)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_27a1.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f520)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f520.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f521)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f521.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f524)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f524.png")));
		sEmojisMap.put(new String(Character.toChars(0x2197)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2197.png")));
		sEmojisMap.put(new String(Character.toChars(0x2196)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2196.png")));
		sEmojisMap.put(new String(Character.toChars(0x2198)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2198.png")));
		sEmojisMap.put(new String(Character.toChars(0x2199)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2199.png")));
		sEmojisMap.put(new String(Character.toChars(0x2194)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2194.png")));
		sEmojisMap.put(new String(Character.toChars(0x2195)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2195.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f504)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f504.png")));
		sEmojisMap.put(new String(Character.toChars(0x25c0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_25c0.png")));
		sEmojisMap.put(new String(Character.toChars(0x25b6)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_25b6.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f53c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f53c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f53d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f53d.png")));
		sEmojisMap.put(new String(Character.toChars(0x21a9)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_21a9.png")));
		sEmojisMap.put(new String(Character.toChars(0x21aa)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_21aa.png")));
		sEmojisMap.put(new String(Character.toChars(0x2139)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2139.png")));
		sEmojisMap.put(new String(Character.toChars(0x23ea)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_23ea.png")));
		sEmojisMap.put(new String(Character.toChars(0x23e9)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_23e9.png")));
		sEmojisMap.put(new String(Character.toChars(0x23eb)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_23eb.png")));
		sEmojisMap.put(new String(Character.toChars(0x23ec)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_23ec.png")));
		sEmojisMap.put(new String(Character.toChars(0x2935)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2935.png")));
		sEmojisMap.put(new String(Character.toChars(0x2934)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2934.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f197)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f197.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f500)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f500.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f501)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f501.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f502)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f502.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f195)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f195.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f199)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f199.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f192)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f192.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f193)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f193.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f196)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f196.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4f6)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4f6.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3a6)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3a6.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f201)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f201.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f22f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f22f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f233)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f233.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f235)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f235.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f234)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f234.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f232)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f232.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f250)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f250.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f239)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f239.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f23a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f23a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f236)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f236.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f21a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f21a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6bb)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6bb.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6b9)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6b9.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6ba)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6ba.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6bc)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6bc.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6be)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6be.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6b0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6b0.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6ae)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6ae.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f17f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f17f.png")));
		// sEmojisMap.put(new String(Character.toChars(0x267f)), new
		// Image(ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath()
		// + "emoji_267f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6ad)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6ad.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f237)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f237.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f238)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f238.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f202)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f202.png")));
		sEmojisMap.put(new String(Character.toChars(0x24c2)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_24c2.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6c2)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6c2.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6c4)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6c4.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6c5)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6c5.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6c3)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6c3.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f251)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f251.png")));
		sEmojisMap.put(new String(Character.toChars(0x3299)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_3299.png")));
		sEmojisMap.put(new String(Character.toChars(0x3297)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_3297.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f191)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f191.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f198)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f198.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f194)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f194.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6ab)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6ab.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f51e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f51e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4f5)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4f5.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6af)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6af.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6b1)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6b1.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6b3)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6b3.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6b7)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6b7.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f6b8)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f6b8.png")));
		sEmojisMap.put(new String(Character.toChars(0x26d4)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_26d4.png")));
		sEmojisMap.put(new String(Character.toChars(0x2733)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2733.png")));
		sEmojisMap.put(new String(Character.toChars(0x2747)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2747.png")));
		sEmojisMap.put(new String(Character.toChars(0x274e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_274e.png")));
		sEmojisMap.put(new String(Character.toChars(0x2705)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2705.png")));
		sEmojisMap.put(new String(Character.toChars(0x2734)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2734.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f49f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f49f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f19a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f19a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4f3)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4f3.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4f4)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4f4.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f170)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f170.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f171)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f171.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f18e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f18e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f17e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f17e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4a0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4a0.png")));
		sEmojisMap.put(new String(Character.toChars(0x27bf)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_27bf.png")));
		sEmojisMap.put(new String(Character.toChars(0x267b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_267b.png")));
		sEmojisMap.put(new String(Character.toChars(0x2648)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2648.png")));
		sEmojisMap.put(new String(Character.toChars(0x2649)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2649.png")));
		sEmojisMap.put(new String(Character.toChars(0x264a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_264a.png")));
		sEmojisMap.put(new String(Character.toChars(0x264b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_264b.png")));
		sEmojisMap.put(new String(Character.toChars(0x264c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_264c.png")));
		sEmojisMap.put(new String(Character.toChars(0x264d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_264d.png")));
		sEmojisMap.put(new String(Character.toChars(0x264e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_264e.png")));
		sEmojisMap.put(new String(Character.toChars(0x264f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_264f.png")));
		sEmojisMap.put(new String(Character.toChars(0x2650)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2650.png")));
		sEmojisMap.put(new String(Character.toChars(0x2651)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2651.png")));
		sEmojisMap.put(new String(Character.toChars(0x2652)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2652.png")));
		sEmojisMap.put(new String(Character.toChars(0x2653)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2653.png")));
		sEmojisMap.put(new String(Character.toChars(0x26ce)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_26ce.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f52f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f52f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f3e7)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f3e7.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4b9)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4b9.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4b2)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4b2.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4b1)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4b1.png")));
		sEmojisMap.put(new String(Character.toChars(0x00a9)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_00a9.png")));
		sEmojisMap.put(new String(Character.toChars(0x00ae)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_00ae.png")));
		sEmojisMap.put(new String(Character.toChars(0x2122)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2122.png")));
		sEmojisMap.put(new String(Character.toChars(0x274c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_274c.png")));
		sEmojisMap.put(new String(Character.toChars(0x203c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_203c.png")));
		sEmojisMap.put(new String(Character.toChars(0x2049)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2049.png")));
		sEmojisMap.put(new String(Character.toChars(0x2757)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2757.png")));
		sEmojisMap.put(new String(Character.toChars(0x2753)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2753.png")));
		sEmojisMap.put(new String(Character.toChars(0x2755)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2755.png")));
		sEmojisMap.put(new String(Character.toChars(0x2754)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2754.png")));
		sEmojisMap.put(new String(Character.toChars(0x2b55)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2b55.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f51d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f51d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f51a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f51a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f519)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f519.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f51b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f51b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f51c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f51c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f503)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f503.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f55b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f55b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f567)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f567.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f550)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f550.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f55c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f55c.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f551)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f551.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f55d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f55d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f552)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f552.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f55e)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f55e.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f553)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f553.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f55f)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f55f.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f554)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f554.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f560)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f560.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f555)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f555.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f556)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f556.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f557)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f557.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f558)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f558.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f559)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f559.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f55a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f55a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f561)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f561.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f562)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f562.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f563)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f563.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f564)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f564.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f565)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f565.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f566)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f566.png")));
		sEmojisMap.put(new String(Character.toChars(0x2716)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2716.png")));
		sEmojisMap.put(new String(Character.toChars(0x2795)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2795.png")));
		sEmojisMap.put(new String(Character.toChars(0x2796)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2796.png")));
		sEmojisMap.put(new String(Character.toChars(0x2797)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2797.png")));
		sEmojisMap.put(new String(Character.toChars(0x2660)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2660.png")));
		sEmojisMap.put(new String(Character.toChars(0x2665)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2665.png")));
		sEmojisMap.put(new String(Character.toChars(0x2663)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2663.png")));
		sEmojisMap.put(new String(Character.toChars(0x2666)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2666.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4ae)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4ae.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f4af)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4af.png")));
		sEmojisMap.put(new String(Character.toChars(0x2714)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2714.png")));
		sEmojisMap.put(new String(Character.toChars(0x2611)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2611.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f518)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f518.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f517)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f517.png")));
		sEmojisMap.put(new String(Character.toChars(0x27b0)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_27b0.png")));
		sEmojisMap.put(new String(Character.toChars(0x3030)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_3030.png")));
		sEmojisMap.put(new String(Character.toChars(0x303d)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_303d.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f531)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f531.png")));
		sEmojisMap.put(new String(Character.toChars(0x25fc)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_25fc.png")));
		sEmojisMap.put(new String(Character.toChars(0x25fb)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_25fb.png")));
		sEmojisMap.put(new String(Character.toChars(0x25fe)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_25fe.png")));
		sEmojisMap.put(new String(Character.toChars(0x25fd)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_25fd.png")));
		sEmojisMap.put(new String(Character.toChars(0x25aa)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_25aa.png")));
		sEmojisMap.put(new String(Character.toChars(0x25ab)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_25ab.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f53a)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f53a.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f532)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f532.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f533)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f533.png")));
		sEmojisMap.put(new String(Character.toChars(0x26ab)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_26ab.png")));
		sEmojisMap.put(new String(Character.toChars(0x26aa)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_26aa.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f534)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f534.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f535)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f535.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f53b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f53b.png")));
		sEmojisMap.put(new String(Character.toChars(0x2b1c)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2b1c.png")));
		sEmojisMap.put(new String(Character.toChars(0x2b1b)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2b1b.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f536)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f536.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f537)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f537.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f538)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f538.png")));
		sEmojisMap.put(new String(Character.toChars(0x1f539)), new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f539.png")));

		// return sEmojisMap;
	}
}
