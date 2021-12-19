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
package com.mallowigi.icons.svgpatchers

import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.SVGLoader
import com.intellij.util.SVGLoader.SvgElementColorPatcher
import com.intellij.util.SVGLoader.SvgElementColorPatcherProvider
import org.w3c.dom.Element
import java.util.SortedSet
import javax.swing.SwingUtilities

/**
 * Main svg patcher: run all registered svg patchers
 *
 * @constructor Create empty Main svg patcher
 */
class MainSvgPatcher : SvgElementColorPatcherProvider {
  private val patchers: SortedSet<SvgPatcher> = sortedSetOf(
    compareByDescending { it.priority() },
    ThemeColorPatcher(),
    AccentColorPatcher(),
    BigIconsPatcher()
  )

  /**
   * Call refresh colors on all patchers
   */
  fun applySvgPatchers() {
    SVGLoader.setColorPatcherProvider(this)
    patchers.forEach { it.refresh() }
    SwingUtilities.invokeLater { ActionToolbarImpl.updateAllToolbarsImmediately() }
  }

  /**
   * Add patcher to the OtherPatcher
   *
   * @param otherPatcher
   */
  fun addPatcher(otherPatcher: SvgElementColorPatcherProvider) {
    patchers.add(OtherSvgPatcher(otherPatcher))
  }

  private fun createPatcher(path: String?): SvgElementColorPatcher {
    return object : SvgElementColorPatcher {
      override fun patchColors(svg: Element) {
        // for each of the internal patchers, patch Colors
        patchers.forEach { it.patch(svg, path) }

        val nodes = svg.childNodes
        val length = nodes.length
        for (i in 0 until length) {
          val item = nodes.item(i)
          if (item is Element) {
            patchColors(item)
          }
        }
      }

      override fun digest(): ByteArray? = null
    }
  }

  override fun forPath(path: String?): SvgElementColorPatcher = createPatcher(path)

  companion object {
    val instance: MainSvgPatcher
      get() = ApplicationManager.getApplication().getService(MainSvgPatcher::class.java)
  }
}