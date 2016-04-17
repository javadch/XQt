package xqt.model.transformation;

import java.util.Map;

public interface QueryFeatureTransformer {
	public String transform(Object feature, Map<String, Object> queryFeatures);
}
