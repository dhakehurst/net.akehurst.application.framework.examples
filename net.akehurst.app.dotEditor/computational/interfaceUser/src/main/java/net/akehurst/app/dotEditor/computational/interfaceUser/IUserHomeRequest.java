package net.akehurst.app.dotEditor.computational.interfaceUser;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface IUserHomeRequest {

	Future<String> fetchParseTree(String initText);

	Future<List<Map<String, Object>>> fetchCompletion(String text, int position);

}
