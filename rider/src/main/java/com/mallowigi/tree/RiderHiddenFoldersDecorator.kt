/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2021 Elior "Mallowigi" Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */
package com.mallowigi.tree

import com.intellij.ide.projectView.PresentationData
import com.intellij.openapi.project.Project
import com.intellij.util.ui.EmptyIcon
import com.jetbrains.rider.projectView.views.solutionExplorer.SolutionExplorerCustomization
import com.jetbrains.rider.projectView.workspace.ProjectModelEntity
import com.jetbrains.rider.projectView.workspace.isDirectory
import com.mallowigi.config.AtomFileIconsConfig

/**
 * Rider Hidden Folders Decorator
 */
class RiderHiddenFoldersDecorator(project: Project) : SolutionExplorerCustomization(project) {
  /**
   * Update icon to hidden
   *
   * @param presentation
   * @param entity
   */
  override fun updateNode(presentation: PresentationData, entity: ProjectModelEntity) {
    super.updateNode(presentation, entity)
    if (!project.isDisposed) {
      when {
        !entity.isDirectory()                           -> return
        !AtomFileIconsConfig.instance.isHideFolderIcons -> return
        else                                            -> setHiddenFolderIcon(presentation)
      }

    }
  }

  private fun setHiddenFolderIcon(presentation: PresentationData) = presentation.setIcon(EmptyIcon.ICON_0)
}