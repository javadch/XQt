package xqt.adapters.dbms.postgresql;

import java.util.Map;

import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.statements.query.ProjectionFeature;
import xqt.model.transformation.QueryFeatureTransformer;

public class PgSProjectionFeatureTransformer implements QueryFeatureTransformer{

	@Override
	public String transform(Object feature, Map<String, Object> queryFeatures) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder(); 
		ProjectionFeature projection = (ProjectionFeature)feature;
		for(PerspectiveAttributeDescriptor attribute: projection.getPerspective().getAttributes().values()){
			//attribute.
			sb.append("Hi");
		}
		return sb.toString();
	}

}
