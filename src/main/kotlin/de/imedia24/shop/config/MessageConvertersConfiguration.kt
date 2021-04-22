package de.imedia24.shop.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport


@Configuration
class MessageConvertersConfiguration(val jsonMergePatchHttpMessageConverter: JsonMergePatchHttpMessageConverter) :
    WebMvcConfigurationSupport() {

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(jsonMergePatchHttpMessageConverter)
        addDefaultHttpMessageConverters(converters)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/swagger-ui/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
            .resourceChain(false)
    }
}