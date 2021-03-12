package tech.claudioed.police.man.grpc;

import static tech.claudioed.police.man.grpc.PoliceOfficerGrpc.getServiceDescriptor;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;


@javax.annotation.Generated(
value = "by VertxGrpc generator",
comments = "Source: policeman.proto")
public final class VertxPoliceOfficerGrpc {
    private VertxPoliceOfficerGrpc() {}

    public static PoliceOfficerVertxStub newVertxStub(io.grpc.Channel channel) {
        return new PoliceOfficerVertxStub(channel);
    }

    
    public static final class PoliceOfficerVertxStub extends io.grpc.stub.AbstractStub<PoliceOfficerVertxStub> {
        private final io.vertx.core.impl.ContextInternal ctx;
        private PoliceOfficerGrpc.PoliceOfficerStub delegateStub;

        private PoliceOfficerVertxStub(io.grpc.Channel channel) {
            super(channel);
            delegateStub = PoliceOfficerGrpc.newStub(channel);
            this.ctx = (io.vertx.core.impl.ContextInternal) io.vertx.core.Vertx.currentContext();
        }

        private PoliceOfficerVertxStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
            delegateStub = PoliceOfficerGrpc.newStub(channel).build(channel, callOptions);
            this.ctx = (io.vertx.core.impl.ContextInternal) io.vertx.core.Vertx.currentContext();
        }

        @Override
        protected PoliceOfficerVertxStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new PoliceOfficerVertxStub(channel, callOptions);
        }

        
        public io.vertx.core.Future<tech.claudioed.police.man.grpc.RegistryID> registry(tech.claudioed.police.man.grpc.MessageData request) {
            return io.vertx.grpc.stub.ClientCalls.oneToOne(ctx, request, delegateStub::registry);
        }

    }

    
    public static abstract class PoliceOfficerVertxImplBase implements io.grpc.BindableService {
        private String compression;

        /**
         * Set whether the server will try to use a compressed response.
         *
         * @param compression the compression, e.g {@code gzip}
         */
        public PoliceOfficerVertxImplBase withCompression(String compression) {
            this.compression = compression;
            return this;
        }

        
        public io.vertx.core.Future<tech.claudioed.police.man.grpc.RegistryID> registry(tech.claudioed.police.man.grpc.MessageData request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                    .addMethod(
                            tech.claudioed.police.man.grpc.PoliceOfficerGrpc.getRegistryMethod(),
                            asyncUnaryCall(
                                    new MethodHandlers<
                                            tech.claudioed.police.man.grpc.MessageData,
                                            tech.claudioed.police.man.grpc.RegistryID>(
                                            this, METHODID_REGISTRY, compression)))
                    .build();
        }
    }

    private static final int METHODID_REGISTRY = 0;

    private static final class MethodHandlers<Req, Resp> implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

        private final PoliceOfficerVertxImplBase serviceImpl;
        private final int methodId;
        private final String compression;

        MethodHandlers(PoliceOfficerVertxImplBase serviceImpl, int methodId, String compression) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
            this.compression = compression;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_REGISTRY:
                    io.vertx.grpc.stub.ServerCalls.oneToOne(
                            (tech.claudioed.police.man.grpc.MessageData) request,
                            (io.grpc.stub.StreamObserver<tech.claudioed.police.man.grpc.RegistryID>) responseObserver,
                            compression,
                            serviceImpl::registry);
                    break;
                default:
                    throw new java.lang.AssertionError();
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new java.lang.AssertionError();
            }
        }
    }

}
