import { AppComponent } from './app.component';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { EditorModule } from 'net.akehurst.language.editor/ng-nal-editor/editor.module'

@NgModule({
  imports: [
    BrowserModule,
    EditorModule
  ],
  declarations: [AppComponent],
  bootstrap: [AppComponent]
})
export class AppModule {
}