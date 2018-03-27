#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform sampler2D blurMap;
uniform vec4 statusColor;

void main() {

    float factor = texture2D(blurMap, vec2(v_texCoords.x, 1 - v_texCoords.y)).a;

    gl_FragColor = texture2D(u_texture, v_texCoords);
    gl_FragColor *= factor * 3;
    gl_FragColor *= statusColor;
}