package net.akehurst.app.dotEditor.engineering.channel.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import net.akehurst.app.dotEditor.computational.interfaceUser.IUserHomeNotification;
import net.akehurst.app.dotEditor.computational.interfaceUser.IUserHomeRequest;
import net.akehurst.application.framework.common.annotations.declaration.ExternalConnection;
import net.akehurst.application.framework.common.interfaceUser.UserSession;
import net.akehurst.application.framework.computational.interfaceUser.authentication.IUserAuthenticationRequest;
import net.akehurst.application.framework.engineering.gui.languageService.GuiLanguageServiceFromProcessor;
import net.akehurst.application.framework.realisation.AbstractIdentifiableObject;
import net.akehurst.application.framework.technology.interfaceGui.GuiEvent;
import net.akehurst.application.framework.technology.interfaceGui.IGuiHandler;
import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.IGuiSceneHandler;
import net.akehurst.application.framework.technology.interfaceGui.SceneIdentity;
import net.akehurst.application.framework.technology.interfaceGui.StageIdentity;
import net.akehurst.application.framework.technology.interfaceGui.data.editor.IGuiLanguageService;
import net.akehurst.language.core.ILanguageProcessor;
import net.akehurst.language.core.analyser.IGrammar;
import net.akehurst.language.core.analyser.ISemanticAnalyser;
import net.akehurst.language.processor.LanguageProcessor;
import net.akehurst.language.processor.OGLanguageProcessor;

public class SceneHandlerHome extends AbstractIdentifiableObject implements IUserHomeNotification, IGuiSceneHandler {

	public SceneHandlerHome(final String afId) {
		super(afId);
	}

	@ExternalConnection
	public IUserHomeRequest userHomeRequest;

	@ExternalConnection
	public IUserAuthenticationRequest userAuthenticationRequest;

	private IHomeScene scene;

	@Override
	public IGuiScene createScene(final IGuiHandler gui, final StageIdentity stageId, final URL content) {
		this.scene = gui.createScene(stageId, SceneHandlerCommon.sceneIdHome, IHomeScene.class, this, content);
		return this.scene;
	}

	@Override
	public void loaded(final IGuiHandler gui, final IGuiScene guiScene, final GuiEvent event) {
		final UserSession session = event.getSession();
		final SceneIdentity currentSceneId = event.getSignature().getSceneId();

		final IGuiLanguageService ql = this.getLanguageService();
		final String initText = this.getExampleDot();
		this.scene.getEditor().add(session, initText, ql);

	}

	String getExampleDot() {
		final InputStream is = this.getClass().getResourceAsStream("/example/html-graph.dot");
		try (final BufferedReader sr = new BufferedReader(new InputStreamReader(is))) {
			final String s = sr.lines().collect(Collectors.joining(System.lineSeparator()));
			return s;
		} catch (final IOException e) {
			e.printStackTrace();
			return "error reading file";
		}
	}

	ILanguageProcessor languageProcessor;

	ILanguageProcessor getLanguageProcessor() {
		if (null == this.languageProcessor) {
			try {
				final OGLanguageProcessor oglProc = new OGLanguageProcessor();
				final InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream("/grammar/Xml.ogl"));
				final IGrammar grammar = oglProc.process(reader, IGrammar.class);
				final ISemanticAnalyser semanticAnalyser = null;
				this.languageProcessor = new LanguageProcessor(grammar, semanticAnalyser);
			} catch (final Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return this.languageProcessor;
	}

	IGuiLanguageService languageService;

	IGuiLanguageService getLanguageService() {
		if (null == this.languageService) {
			final ILanguageProcessor processor = this.getLanguageProcessor();
			this.languageService = new GuiLanguageServiceFromProcessor(processor);
		}
		return this.languageService;

	}
}
