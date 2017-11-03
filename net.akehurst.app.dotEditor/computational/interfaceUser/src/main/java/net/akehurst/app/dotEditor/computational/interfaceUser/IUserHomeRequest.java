package net.akehurst.app.dotEditor.computational.interfaceUser;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import net.akehurst.app.dotEditor.computational.interfaceUser.data.DotGraph;

public interface IUserHomeRequest {

	Future<String> fetchParseTree(String initText);

	Future<List<Map<String, Object>>> fetchCompletion(String text, int position);

	Future<DotGraph> fetchGraph(String dotText);

}
