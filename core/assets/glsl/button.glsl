#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float x, press;

void main() {

    gl_FragColor = texture2D(u_texture, v_texCoords);

    float factor = 1 - gl_FragColor.a * 3;
    float min = (v_texCoords.x - x) * 2;
    if (min < 0)
        factor -= min;
    else
        factor += min;

    factor = (1 - factor) * press + factor * (1 - press);

    gl_FragColor.a *= 1 - factor;

}