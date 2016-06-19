package xqt.adapters.dbms.postgresql;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.vaiona.commons.data.AttributeInfo;

import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.statements.query.ProjectionFeature;
import xqt.model.transformation.QueryFeatureTransformer;

public class PgSProjectionFeatureTransformer implements QueryFeatureTransformer{

	@Override
	public String transform(Object feature, Map<String, Object> queryFeatures) {
		if(feature == null)
			return null;

		StringJoiner sj = new StringJoiner(",", "", "");
		List<AttributeInfo> attributInfos = (List<AttributeInfo>)feature;
		for(AttributeInfo attributeInfo: attributInfos){
			StringBuilder attributeSb = new StringBuilder();
			//experimental code
			if(attributeInfo.forwardMapTranslated != null && !attributeInfo.forwardMapTranslated.isEmpty())
				attributeSb.append(attributeInfo.forwardMapTranslated);
			else
				attributeSb.append(attributeInfo.forwardMap); //Should not work properly, but for testing purposes.
			attributeSb .append(" AS ");
			attributeSb.append(attributeInfo.name);
			sj.add(attributeSb.toString());
		}
		return sj.toString();
	}

}
