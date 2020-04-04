/*
 * Copyright (c) 2009-2016 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package external;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

import com.jme3.terrain.heightmap.HeightMap;
import com.jme3.texture.Image;
import com.jme3.util.BufferUtils;

/**
 * <p><code>HeightBasedAlphaMapGenerator</code> is used for generating alpha maps 
 * based on a heightmap and given thresholds for the colors.</p>
 *
 * <p>
 * 	<b>Default values:</b>
 * 	<ul>
 * 		<li>0f   -  50f = Red   (Tex1)</li>
 * 		<li>50f  - 100f = Green (Tex2)</li>
 * 		<li>100f - 200f = Blue  (Tex3)</li>
 *	</ul>
 * </p>
 *
 * @author Sebastian Teumert 
 * 	(<a href="https://github.com/NetzwergX">github.com/NetzwergX</a>)
 *
 */
public class HeightBasedAlphaMapGenerator {

	/* ----- Variables ----- */

	private HeightMap heightmap;

	private WeakReference<Image> alphamap;
	private boolean rendered = false;

	private float tex1Height = 0;
	private float tex2Height = 50;
	private float tex3Height = 100;
	private float maxHeight  = 200;

	/* ----- Constructor ----- */

	/**
	 * Constructs a new <code>HeightBasedAlphaMapGenerator</code> for the
	 * given heightmap
	 * @param heightMap the heightmap to be used
	 */
	public HeightBasedAlphaMapGenerator(HeightMap heightMap) {
		setHeightmap(heightMap);
	}

	/**
	 * Constructs a new <code>HeightBasedAlphaMapGenerator</code> for the
	 * given heightmap and color thresholds. 
	 * @param heightMap		the underlying heightmap
	 * @param tex1Height 	minimal height for tex1 (red)
	 * @param tex2Height	minimal height for tex2 (green)
	 * @param tex3Height	minimal height for tex3 (blue)
	 * @param maxHeight		maximal height (else alpha = 0)
	 */
	public HeightBasedAlphaMapGenerator(HeightMap heightMap,
			float tex1Height, float tex2Height,
			float tex3Height, float maxHeight) {
		this(heightMap);
		setTex1Height(tex1Height);
		setTex2Height(tex2Height);
		setTex3Height(tex3Height);
		setMaxHeight(maxHeight);
	}

	/* ----- Methods ----- */

	public HeightMap getHeightmap() {
		return heightmap;
	}

	public void setHeightmap(HeightMap heightmap) {
		rendered = rendered && (heightmap == this.heightmap);
		this.heightmap = heightmap;
	}

	public float getTex1Height() {
		return tex1Height;
	}

	public void setTex1Height(float tex1Height) {
		rendered = rendered && (this.tex1Height == tex1Height);
		this.tex1Height = tex1Height;
	}

	public float getTex2Height() {
		return tex2Height;
	}

	public void setTex2Height(float tex2Height) {
		rendered = rendered && (this.tex2Height == tex2Height);
		this.tex2Height = tex2Height;
	}

	public float getTex3Height() {
		return tex3Height;
	}

	public void setTex3Height(float tex3Height) {
		rendered = rendered && (this.tex3Height == tex3Height);
		this.tex3Height = tex3Height;
	}

	public float getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(float maxHeight) {
		rendered = rendered && (this.maxHeight == maxHeight);
		this.maxHeight = maxHeight;
	}

	/**
	 * Renders and returns the alpha map. Subsequent calls only re-generate the
	 * image if one of the height values are changed, otherwise a cached version 
	 * is returned.
	 *
	 * @return the rendered alpha map
	 */
	public Image renderAlphaMap() {
		if (rendered && alphamap.get() != null)
			return alphamap.get();

		int height = heightmap.getSize();
		int width = heightmap.getSize();

		ByteBuffer data = BufferUtils.createByteBuffer(width * height * 4);

		for (int x = 0; x < width; x++) {
			for (int z = 0; z < height; z++) {

				int alpha = 255, red = 0, green = 0, blue = 0;

				float pointHeight = heightmap.
													 getScaledHeightAtPoint(z, width - (x+1));

				if (pointHeight < maxHeight) {
					if (pointHeight > tex3Height)
						blue = 255;
					else if (pointHeight > tex2Height)
						green = 255;
					else if (pointHeight > tex1Height)
						red = 255;
					else if (pointHeight < tex1Height)
						alpha = 0;
				}
				else
					alpha = 0;
				data
						.put((byte) red)
						.put((byte) green)
						.put((byte) blue)
						.put((byte) alpha);
			}
		}

		Image image = new Image(Image.Format.RGBA8, width, height, data);
		alphamap = new WeakReference<Image>(image);
		rendered = true;
		return image;
	}
}