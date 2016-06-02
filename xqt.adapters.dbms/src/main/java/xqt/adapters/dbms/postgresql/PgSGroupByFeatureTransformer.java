package xqt.adapters.dbms.postgresql;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.vaiona.commons.data.AttributeInfo;

import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.statements.query.ProjectionFeature;
import xqt.model.transformation.QueryFeatureTransformer;

public class PgSGroupByFeatureTransformer implements QueryFeatureTransformer{

	@Override
	public String transform(Object feature, Map<String, Object> queryFeatures) {
		StringJoiner sj = new StringJoiner(",", "", "");
		return sj.toString();
	}

}
