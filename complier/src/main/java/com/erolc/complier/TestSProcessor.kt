package com.erolc.complier

import com.erolc.annotations.Test
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.writeTo

class TestSProcessor(private val environment: SymbolProcessorEnvironment) :
    SymbolProcessor {
    private val logger = environment.logger
    override fun process(resolver: Resolver): List<KSAnnotated> {
        KspContext.environment = environment
        KspContext.resolver = resolver

        logger.logging("test log run")//
        logger.info("test info run")
        logger.warn("test warn run")
//        logger.error("test error info")

        resolver.getSymbolsWithAnnotation(Test::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .toSet().forEach {
                FileSpec.builder(
                    it.packageName.asString(),
                    "${it.simpleName.asString()}$\$Test"
                ).addFunction(FunSpec.builder("test")
                    .addStatement("return 12")
                    .build())
                    .build().writeTo(KspContext.environment.codeGenerator, false)
            }

        return emptyList()
    }

}
