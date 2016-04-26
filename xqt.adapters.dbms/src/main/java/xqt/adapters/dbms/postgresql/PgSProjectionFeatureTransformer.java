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
		// TODO Auto-generated method stub
//		StringBuilder sb = new StringBuilder(); 
//		ProjectionFeature projection = (ProjectionFeature)feature;
//		for(PerspectiveAttributeDescriptor attribute: projection.getPerspective().getAttributes().values()){
//			//attribute.
//			sb.append("Hi");
//		}
		// it can be that I was wrong with the ProjectionFeatureCLass
		// try following too
		StringJoiner sj = new StringJoiner(",", "", "");
		List<AttributeInfo> attributInfos = (List<AttributeInfo>)feature;
		for(AttributeInfo attributeInfo: attributInfos){
			StringBuilder attributeSb = new StringBuilder();
			attributeSb.append(attributeInfo.forwardMapTranslated);
			attributeSb .append(" AS ");
			attributeSb.append(attributeInfo.name);
			sj.add(attributeSb.toString());
		}
		return sj.toString();
	}

}
