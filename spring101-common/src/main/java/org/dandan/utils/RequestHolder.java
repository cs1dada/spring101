/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dandan.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * 获取 HttpServletRequest
 * @author Zheng Jie
 * @date 2018-11-24
 */
public class RequestHolder {
    /**
     * 你的 RequestHolder 類別使用了 Spring Framework 的 RequestContextHolder 來獲取當前的 HttpServletRequest。
     * 這是一種在應用程式中存儲和檢索當前線程的 HttpServletRequest 的方式。這對於在沒有直接參數傳遞的情況下，
     * 例如在一個方法調用鏈中，獲取當前 HTTP 請求是非常方便的。
     *
     * 這裡是一個簡單的解釋：
     *
     * RequestContextHolder： Spring 的 RequestContextHolder 是一個持有上下文資訊的 Holder 類別，可以用來存儲當前執行緒中的請求或會話上下文。
     *
     * ServletRequestAttributes： 這是 Spring 提供的一個實現了 RequestAttributes 介面的類別，用於存儲 Servlet 特定的屬性。
     * 在這裡，你使用了它來獲取當前的 HttpServletRequest。
     *
     * getHttpServletRequest() 方法： 這個方法返回當前執行緒的 HttpServletRequest。
     * 如果 RequestAttributes 為空，或者無法轉換為 ServletRequestAttributes，這個方法會拋出 NullPointerException。
     *
     * 簡而言之，這個 RequestHolder 類別的目的是提供一個方便的方式來獲取當前的 HttpServletRequest，而無需在每個方法中進行參數傳遞。
     * 這在需要處理 HTTP 請求相關資訊時非常實用。
     * 
     * @return
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}
