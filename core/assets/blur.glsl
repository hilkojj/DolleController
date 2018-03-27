#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {

    for (int x = -25; x < 25; x++) {
        for (int y = -25; y < 25; y++) {
            gl_FragColor += texture2D(
                u_texture, vec2(v_texCoords.x + x * .004f, v_texCoords.y + y * .008f)
            );
        }
    }

    gl_FragColor = vec4(1, 1, 1, gl_FragColor.a / 800);

}