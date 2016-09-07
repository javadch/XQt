package xqt.adapters.dbms.postgresql;

import java.util.Map;

import xqt.model.transformation.QueryFeatureTransformer;

public class PgSSelectionFeatureTransformer implements QueryFeatureTransformer{

	@Override
	public String transform(Object feature, Map<String, Object> queryFeatures) {
		if(feature == null)
			return null;
		String where = feature.toString();
		if (where.length() <=0)
			return null;
		return where;
	}
}
