#version 400 core

const int maxLights = 4;

in vec2 passTextureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[maxLights];
in vec3 toCameraVector;
in float visibility;

out vec4 outColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor[maxLights];
uniform vec3 attenuation[maxLights];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor; 

void main(void) {

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for(int i = 0; i < maxLights; i++) {
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);	
		float nDot1 = dot(unitNormal, unitLightVector);
		float brightness = max(nDot1, 0.0);	
		
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);	
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);
		
		totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / attFactor;	
		totalSpecular = totalSpecular + (dampedFactor *  reflectivity * lightColor[i]) / attFactor;
	}
	
	totalDiffuse = max(totalDiffuse, 0.08);
	
	vec4 textureColor = texture(textureSampler, passTextureCoords);
	if(textureColor.a < 0.5) {
		discard;
	}

	outColor = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
	outColor = mix(vec4(skyColor, 1.0), outColor, visibility);

}